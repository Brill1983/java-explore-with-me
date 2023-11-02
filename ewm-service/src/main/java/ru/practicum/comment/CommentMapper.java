package ru.practicum.comment;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentShortDto toShortDto(Comment comment, EventShortDto eventDto) {
        return new CommentShortDto(
                comment.getId(),
                comment.getText(),
                eventDto,
                comment.getCreatedOn()
        );
    }

    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return new Comment(
                null,
                commentDto.getText(),
                user,
                event,
                LocalDateTime.now()
        );
    }

    public static CommentFullDto toFullDto(Comment comment, UserShortDto user, EventShortDto eventDto) {
        return new CommentFullDto(
                comment.getId(),
                comment.getText(),
                user,
                eventDto,
                comment.getCreatedOn()
        );
    }
}
