package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ElementNotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService{

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUsersRequests(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));

        return requestRepository.findAllByRequester_Id(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto postRequest(long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException("Собятия с ID: " + eventId + " не найден"));
        Optional<Request> optionalEvent = requestRepository.findByRequester_IdAndEvent_Id(userId, eventId);
        if (optionalEvent.isPresent()) {
            throw new ConflictException("Запрос от пользователя с ID: " + userId +
                    ", на мероприятие с ID: " + eventId + "уже зарегистрирован");
        }
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Мероприятие с ID: " + eventId + ", создано пользователем ID: " + userId +
                    ", нельзя делать запрос на свое мероприятие");
        }
        if (event.getPublishedOn() != null) {
            throw new ConflictException("Мероприятие с ID: " + eventId + ", не опуликовано");
        }
        Integer participantsNumber = requestRepository.countAllByStatusAndEvent_Id(Status.CONFIRMED, eventId);

        if (participantsNumber != null && participantsNumber >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ConflictException("На мероприятие с ID: " + eventId + ", уже зарегистрировано максимальное кол-во участников");
        }

        Request request = new Request(0L, LocalDateTime.now(), event, user, Status.PENDING);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        }

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto patchRequest(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ElementNotFoundException("Запроса с ID: " + requestId + " нет в базе"));

        if (request.getRequester().getId() != userId) {
            throw new ConflictException("Запрос с ID: " + requestId + ", создан не пользователем ID: " + userId);
        }
        request.setStatus(Status.CANCELED);
        return RequestMapper.toDto(requestRepository.save(request));
    }
}
