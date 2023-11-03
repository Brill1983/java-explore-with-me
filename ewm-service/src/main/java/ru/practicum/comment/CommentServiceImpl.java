package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.EventRepository;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ElementNotFoundException;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserRepository;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 1. Разместить комментарий можно только к опубликованному событию.
 * 2. Разместить комментарий можно к событию, которое уже закончилось или еще не началось.
 * 3. В коротком представлении события (EventShortDto) должно присутствовать количество комментариев.
 * 4. Исправлять комментарий может только автор комментария.
 * 5. Удалять комментарий может как автор, так и администратор.
 * 6. По запросам автора комментария (через PrivateController) выводится информация в укороченном виде - без поля
 * автора комментария (CommentShortDto).
 * 7. По запрсоам неавторизованного пользователя (серез PublicController) выводится информация в полном виде с
 * указанием автора комментария (CommentFullDto), кроме запроса на все комментарии пользователя с указанным ID (в этом
 * случае выводится информация в укороченном виде - без поля автора комментария (CommentShortDto).
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;


    // _______________________________ Private _________________________________________________________________________
    @Transactional
    @Override
    public CommentShortDto postComment(long userId, long eventId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException("События с ID: " + eventId + " не найдено"));
        if (event.getPublishedOn() == null) {
            throw new ConflictException("Мероприятие с ID: " + eventId + ", не опуликовано");
        }

        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, user, event));

        return mapShortCommentDto(comment, event);
    }

    @Transactional
    @Override
    public CommentShortDto patchComment(long userId, long commentId, CommentDto commentDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ElementNotFoundException("Комментарий с ID: " + commentId + " не найден"));
        if (comment.getAuthor().getId() != userId) {
            throw new ConflictException("Комментарий с ID " + commentId + " размещен не пользователем с ID " + userId +
                    ". Изменение чужого комментария запрещено");
        }
        comment.setText(commentDto.getText());

        Comment updatedComment = commentRepository.save(comment);

        return mapShortCommentDto(updatedComment, comment.getEvent());
    }

    @Transactional
    @Override
    public void deleteCommentByOwner(long userId, long commentId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ElementNotFoundException("Комментарий с ID: " + commentId + " не найден"));
        if (comment.getAuthor().getId() != userId) {
            throw new ConflictException("Комментарий с ID " + commentId + " размещен не пользователем с ID " + userId +
                    ". Удаление чужого комментария запрещено");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentShortDto> getOwnerCommentsForEvent(long userId, long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException("События с ID: " + eventId + " не найдено"));

        EventShortDto eventShortDto = eventService.mapEventsToShortDtos(List.of(event)).get(0);
        return commentRepository.findByAuthor_IdAndEvent_Id(userId, eventId).stream()
                .map(comment -> CommentMapper.toShortDto(comment, eventShortDto))
                .collect(Collectors.toList());
    }

    // _______________________________ Public _________________________________________________________________________
    @Override
    public CommentFullDto getCommentById(long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ElementNotFoundException("Комментарий с ID: " + commentId + " не найден"));

        UserShortDto userDto = UserMapper.toUserShortDto(comment.getAuthor());

        EventShortDto eventShortDto = eventService.mapEventsToShortDtos(List.of(comment.getEvent())).get(0);
        return CommentMapper.toFullDto(comment, userDto, eventShortDto);
    }

    @Override
    public List<CommentShortDto> getUserComments(long userId, Integer from, Integer size, String sort) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));

        List<Comment> commentList = commentRepository.findAllByAuthor_Id(userId, makePage(from, size, sort)).toList();
        List<Event> eventsList = commentList.stream()
                .map(Comment::getEvent)
                .collect(Collectors.toList());
        List<EventShortDto> eventShortDtoList = eventService.mapEventsToShortDtos(eventsList);

        Map<Long, EventShortDto> enentsMap = new HashMap<>();
        for (EventShortDto eventShortDto : eventShortDtoList) {
            enentsMap.put(eventShortDto.getId(), eventShortDto);
        }

        List<CommentShortDto> commentDtosList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtosList.add(CommentMapper.toShortDto(comment, enentsMap.get(comment.getEvent().getId())));
        }
        return commentDtosList;
    }

    @Override
    public List<CommentFullDto> getCommentsByEventId(long eventId, Integer from, Integer size, String sort) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException("События с ID: " + eventId + " не найдено"));

        EventShortDto eventShortDto = eventService.mapEventsToShortDtos(List.of(event)).get(0);

        List<Comment> commentList = commentRepository.findAllByEvent_Id(eventId, makePage(from, size, sort)).toList();

        List<UserShortDto> users = commentList.stream()
                .map(Comment::getAuthor)
                .map(UserMapper::toUserShortDto)
                .collect(Collectors.toList());

        Map<Long, UserShortDto> usersMap = new HashMap<>();
        for (UserShortDto user : users) {
            usersMap.put(user.getId(), user);
        }

        List<CommentFullDto> commentFullDtos = new ArrayList<>();
        for (Comment comment : commentList) {
            commentFullDtos.add(CommentMapper.toFullDto(comment, usersMap.get(comment.getAuthor().getId()), eventShortDto));
        }
        return commentFullDtos;
    }

    // _______________________________ Admin _________________________________________________________________________
    @Transactional
    @Override
    public void deleteCommentByAdmin(long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new ElementNotFoundException("Комментарий с ID: " + commentId + " не найден"));

        commentRepository.deleteById(commentId);
    }

    // _______________________________ Utils _________________________________________________________________________
    private Pageable makePage(Integer from, Integer size, String sort) {
        if (CommentSort.valueOf(sort.toUpperCase()).equals(CommentSort.DESC)) {
            return PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        } else {
            return PageRequest.of(from / size, size, Sort.by("createdOn").ascending());
        }
    }

    private CommentShortDto mapShortCommentDto(Comment comment, Event event) {
        EventShortDto eventShortDto = eventService.mapEventsToShortDtos(List.of(event)).get(0);
        return CommentMapper.toShortDto(comment, eventShortDto);
    }
}
