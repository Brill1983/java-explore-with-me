package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.exceptions.BadParameterException;
import ru.practicum.utils.SortConstrain;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Constants.DATE_FORMAT;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getPublicEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE") @SortConstrain String sort,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        log.info("В метод getPublicEvents переданы данные: text = {}, categories = {}, paid = {}, rangeStart = {}, " +
                        "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        LocalDateTime start = !StringUtils.isEmpty(rangeStart) ? LocalDateTime.parse(rangeStart, DATE_FORMAT) : null;
        LocalDateTime end = !StringUtils.isEmpty(rangeEnd) ? LocalDateTime.parse(rangeEnd, DATE_FORMAT) : null;

        if ((end != null && start != null) && end.isBefore(start)) {
            throw new BadParameterException("Начало не может быть позже окончания периода");
        }

        return eventService.getPublicEvents(text, categories, paid, start, end, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@PathVariable long id,
                                           HttpServletRequest request) {
        log.info("В метод getPublicEventById переданы данные: id = {}", id);
        return eventService.getPublicEventById(id, request);
    }
}
