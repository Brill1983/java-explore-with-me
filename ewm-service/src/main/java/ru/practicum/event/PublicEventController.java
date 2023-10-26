package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.exceptions.BadParameterException;
import ru.practicum.utils.SortConstrain;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Constants.DATE_FORMAT;

@Slf4j
@RestController
@RequestMapping(path = "/event")
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
                                               @RequestParam(defaultValue = "10") int size) {
        log.info("В метод getPublicEvents переданы данные: text = {}, categories = {}, paid = {}, rangeStart = {}, " +
                "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, DATE_FORMAT) : null;
        LocalDateTime end = rangeStart != null ? LocalDateTime.parse(rangeEnd, DATE_FORMAT) : null;

        if ((end != null && start != null) && end.isBefore(start)) {
            throw new BadParameterException("Начало не может быть позже окончания периода");
        }

        return eventService.getPublicEvents(text, categories, paid, start, end, onlyAvailable, sort, from, size);
    }
}
