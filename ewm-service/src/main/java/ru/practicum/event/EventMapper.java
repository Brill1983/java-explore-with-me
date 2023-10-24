package ru.practicum.event;

import lombok.experimental.UtilityClass;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.model.Location;
import ru.practicum.user.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_FORMAT;

@UtilityClass
public class EventMapper {

    public Event toEvent(NewEventDto newEventDto, User user, Category category, Location location) {
        return Event.builder()
                .id(0L)
                .initiator(user)
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .location(location)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate() != null ?
                        LocalDateTime.parse(newEventDto.getEventDate(), DATE_FORMAT) : null)
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .state(State.PENDING)
                .createdOn(LocalDateTime.now())
                .publishedOn(null)
                .build();
    }

    public EventShortDto toShortDto(Event event, Integer confirmedRequests, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory() != null ? CategoryMapper.toCategoryDto(event.getCategory()) : null)
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate() != null ? event.getEventDate().format(DATE_FORMAT) : null)
                .initiator(event.getInitiator() != null ? UserMapper.toUserShortDto(event.getInitiator()) : null)
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
