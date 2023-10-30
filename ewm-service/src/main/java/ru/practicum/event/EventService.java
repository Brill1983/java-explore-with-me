package ru.practicum.event;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getCurrentUserEvents(long userId, Integer from, Integer size);

    EventFullDto postEvent(long userId, NewEventDto eventDto);

    EventFullDto getOwnerEvent(long userId, long eventId);

    EventFullDto patchCurrentUserEvent(long userId, long eventId, UpdateEventUserRequest eventDto);

    List<ParticipationRequestDto> getRequestsForOwnersEvent(long userId, long eventId);

    EventRequestStatusUpdateResult patchRequestsForOwnersEvent(long userId, long eventId,
                                                               EventRequestStatusUpdateRequest updateRequest);

    List<EventFullDto> getAdminFullEvent(List<Long> users, List<String> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto patchAdminEvent(long eventId, UpdateEventAdminRequest eventDto);

    List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                                        LocalDateTime end, boolean onlyAvailable, String sort, int from, int size,
                                        HttpServletRequest request);

    EventFullDto getPublicEventById(long eventId, HttpServletRequest request);

    List<EventShortDto> mapEventsToShortDtos(List<Event> events);

}
