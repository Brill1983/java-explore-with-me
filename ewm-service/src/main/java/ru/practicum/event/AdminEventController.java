package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.exceptions.BadParameterException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Constants.DATE_FORMAT;

@Slf4j
@RestController
@RequestMapping(path = "/admin/event")
@RequiredArgsConstructor
@Validated
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getFullEvent(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<String> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("В метод getFullEvent переданы данные: users = {}, states = {}, categories = {}, rangeStart = {}, " +
                "rangeEnd = {}, from = {}, size = {}", users, states, categories, rangeStart, rangeEnd, from, size);

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, DATE_FORMAT) : null;
        LocalDateTime end = rangeStart != null ? LocalDateTime.parse(rangeEnd, DATE_FORMAT) : null;

        if ((end != null && start != null) && end.isBefore(start)) {
            throw new BadParameterException("Начало не может быть позже окончания периода");
        }

        return eventService.getAdminFullEvent(users, states, categories, start, end, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable long eventId,
                                   @RequestBody UpdateEventAdminRequest eventDto) {
        log.info("В метод patchEvent переданы данные: eventId = {}, eventDto = {}", eventId, eventDto);

        return eventService.patchAdminEvent(eventId, eventDto);
    }
}
