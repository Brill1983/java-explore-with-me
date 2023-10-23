package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public EventShortDto getCurrentUserEvents(@PathVariable long userId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("В метод getCurrentUserEvents переданы данные: userId = {}, from = {}, size = {}", userId, from, size);
        return eventService.getCurrentUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto postEvent(@PathVariable long userId,
                                  @RequestBody @Valid NewEventDto eventDto) {
        log.info("В метод postEvent переданы данные: userId = {}, eventDto = {}=", userId, eventDto);
        return eventService.postEvent(userId, eventDto);
    }
}
