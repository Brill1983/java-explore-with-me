package ru.practicum.event;

import lombok.experimental.UtilityClass;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

@UtilityClass
public class EventMapper {

    public Event toEvent(NewEventDto newEventDto, User user) {
//        return new Event()
//                .setId(0L)
//                ;
        return null;
    }
}
