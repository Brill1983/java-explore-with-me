package ru.practicum.event;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getCurrentUserEvents(long userId, Integer from, Integer size);

    EventFullDto postEvent(long userId, NewEventDto eventDto);
}
