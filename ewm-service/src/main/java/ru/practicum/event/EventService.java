package ru.practicum.event;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getCurrentUserEvents(long userId, Integer from, Integer size);

    EventFullDto postEvent(long userId, NewEventDto eventDto);

    EventFullDto getOwnerEvent(long userId, long eventId);

    EventFullDto patchCurrentUserEvent(long userId, long eventId, UpdateEventUserRequestDto eventDto);

    List<ParticipationRequestDto> getRequestsForOwnersEvent(long userId, long eventId);

    EventRequestStatusUpdateResult patchRequestsForOwnersEvent(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);
}
