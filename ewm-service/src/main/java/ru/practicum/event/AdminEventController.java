package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.AdminGetEventParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getFullEvent(@Valid AdminGetEventParams params) {
        log.info("В метод getFullEvent переданы данные: params = {}", params);
        return eventService.getAdminFullEvent(params);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable long eventId,
                                   @RequestBody @Valid UpdateEventAdminRequest eventDto) {
        log.info("В метод patchEvent переданы данные: eventId = {}, eventDto = {}", eventId, eventDto);

        return eventService.patchAdminEvent(eventId, eventDto);
    }
}
