package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.PublicGetEventParams;
import ru.practicum.utils.validations.StartBeforeEndConstrain;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getPublicEvents(@Valid @StartBeforeEndConstrain PublicGetEventParams params,
                                               HttpServletRequest request) {
        log.info("В метод getPublicEvents переданы данные: params = {}", params);
        return eventService.getPublicEvents(params, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@PathVariable long id,
                                           HttpServletRequest request) {
        log.info("В метод getPublicEventById переданы данные: id = {}", id);
        return eventService.getPublicEventById(id, request);
    }
}
