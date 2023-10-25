package ru.practicum.event;

import lombok.experimental.UtilityClass;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequestDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.LocationMapper;
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

    public EventFullDto toFullDto(Event event, Integer confirmedRequests, Long veiws) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn().format(DATE_FORMAT))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DATE_FORMAT))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn().format(DATE_FORMAT))
                .requestModeration(event.getRequestModeration())
                .state(event.getState().name())
                .title((event.getTitle()))
                .views(veiws)
                .build();
    }

    public Event toEventFromUpdateDto(Event event, UpdateEventUserRequestDto eventDto, Category category) {
        Event eventFromDto = Event.builder()
                .id(event.getId())
                .initiator(event.getInitiator())
                .category(category)
                .description(eventDto.getDescription() != null ? eventDto.getDescription() : event.getDescription())
                .eventDate(eventDto.getEventDate() != null ? LocalDateTime.parse(eventDto.getEventDate(), DATE_FORMAT) :
                        event.getEventDate())
                .location(eventDto.getLocation() != null ? LocationMapper.toModel(eventDto.getLocation()) :
                        event.getLocation())
                .paid(eventDto.getPaid() != null ? eventDto.getPaid() : event.getPaid())
                .participantLimit(eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() :
                        event.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration() != null ? eventDto.getRequestModeration() :
                        event.getRequestModeration())
                .title(eventDto.getTitle() != null ? eventDto.getTitle() : event.getTitle())
                .build();
        if (eventDto.getStateAction() != null) {
            eventFromDto.setState(StateAction.valueOf(eventDto.getStateAction()).equals(StateAction.SEND_TO_REVIEW) ?
                    State.PENDING : State.CANCELED);
        } else {
            eventFromDto.setState(event.getState());
        }
        return eventFromDto;
    }
}
