package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getCurrentUsersEvents(@PathVariable long userId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("В метод getCurrentUsersEvents переданы данные: userId = {}, from = {}, size = {}", userId, from, size);
        return eventService.getCurrentUserEvents(userId, from, size);
    }

    @PostMapping
    public EventFullDto postEvent(@PathVariable long userId,
                                  @RequestBody @Valid NewEventDto eventDto) {
        log.info("В метод postEvent переданы данные: userId = {}, eventDto = {}", userId, eventDto);
        return eventService.postEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getOwnerEvent(@PathVariable long userId,
                                      @PathVariable long eventId) {
        log.info("В метод getCurrentUserEvents переданы данные: userId = {}, eventId = {}", userId, eventId);
        return eventService.getOwnerEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchCurrentUserEvent(@PathVariable long userId,
                                              @PathVariable long eventId,
                                              @RequestBody @Valid UpdateEventUserRequest eventDto) {
        log.info("В метод patchCurrentUserEvent переданы данные: userId = {}, eventId = {}, eventDto = {}",
                userId, eventId, eventDto);
        return eventService.patchCurrentUserEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForOwnersEvent(@PathVariable long userId,
                                                                   @PathVariable long eventId) {
        log.info("В метод getRequestsForOwnersEvent переданы данные: userId = {}, eventId = {}", userId, eventId);
        return eventService.getRequestsForOwnersEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequestsForOwnersEvent(@PathVariable long userId,
                                                                      @PathVariable long eventId,
                                                                      @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("В метод patchRequestsForOwnersEvent переданы данные: userId = {}, eventId = {}, updateRequest = {}",
                userId, eventId, updateRequest);
        return eventService.patchRequestsForOwnersEvent(userId, eventId, updateRequest);
    }
}
