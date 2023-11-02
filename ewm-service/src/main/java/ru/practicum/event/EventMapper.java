package ru.practicum.event;

import lombok.experimental.UtilityClass;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
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
                .paid(newEventDto.getPaid() != null ? newEventDto.getPaid() : false)
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration() : true)
                .title(newEventDto.getTitle())
                .state(State.PENDING)
                .createdOn(LocalDateTime.now())
                .publishedOn(null)
                .build();
    }

    public EventShortDto toShortDto(Event event, Integer confirmedRequests, Long views, Long commentsQuantity) {
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
                .commentsQuantity(commentsQuantity)
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
                .publishedOn(event.getPublishedOn() != null ? event.getPublishedOn().format(DATE_FORMAT) : null)
                .requestModeration(event.getRequestModeration())
                .state(event.getState().name())
                .title((event.getTitle()))
                .views(veiws)
                .build();
    }

    public Event toEventFromUserUpdateDto(Event event, UpdateEventUserRequest eventDto, Category category, Location location) {
        Event eventFromDto = Event.builder()
                .id(event.getId())
                .initiator(event.getInitiator())
                .annotation(eventDto.getAnnotation() != null ? eventDto.getAnnotation() : event.getAnnotation())
                .category(category != null ? category : event.getCategory())
                .description(eventDto.getDescription() != null ? eventDto.getDescription() : event.getDescription())
                .eventDate(eventDto.getEventDate() != null ? LocalDateTime.parse(eventDto.getEventDate(), DATE_FORMAT) :
                        event.getEventDate())
                .location(location != null ? location : event.getLocation())
                .paid(eventDto.getPaid() != null ? eventDto.getPaid() : event.getPaid())
                .participantLimit(eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() :
                        event.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration() != null ? eventDto.getRequestModeration() :
                        event.getRequestModeration())
                .title(eventDto.getTitle() != null ? eventDto.getTitle() : event.getTitle())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .build();
        if (eventDto.getStateAction() != null) {
            eventFromDto.setState(
                    UserStateAction.valueOf(eventDto.getStateAction())
                            .equals(UserStateAction.SEND_TO_REVIEW) ?
                            State.PENDING :
                            State.CANCELED
            );
        } else {
            eventFromDto.setState(event.getState());
        }
        return eventFromDto;
    }

    public Event toEventFromAdminUpdateDto(Event event, UpdateEventAdminRequest eventDto, Category category, Location location) {
        Event eventFromDto = Event.builder()
                .id(event.getId())
                .initiator(event.getInitiator())
                .annotation(eventDto.getAnnotation() != null ? eventDto.getAnnotation() : event.getAnnotation())
                .category(category != null ? category : event.getCategory())
                .description(eventDto.getDescription() != null ? eventDto.getDescription() : event.getDescription())
                .eventDate(eventDto.getEventDate() != null ? LocalDateTime.parse(eventDto.getEventDate(), DATE_FORMAT) :
                        event.getEventDate())
                .location(location != null ? location : event.getLocation())
                .paid(eventDto.getPaid() != null ? eventDto.getPaid() : event.getPaid())
                .participantLimit(eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() :
                        event.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration() != null ? eventDto.getRequestModeration() :
                        event.getRequestModeration())
                .title(eventDto.getTitle() != null ? eventDto.getTitle() : event.getTitle())
                .createdOn(event.getCreatedOn())
                .build();
        if (eventDto.getStateAction() != null) {
            if (AdminStateAction.valueOf(eventDto.getStateAction()).equals(AdminStateAction.PUBLISH_EVENT)) {
                eventFromDto.setState(State.PUBLISHED);
                eventFromDto.setPublishedOn(LocalDateTime.now());
            } else {
                eventFromDto.setState(State.CANCELED);
            }
        } else {
            eventFromDto.setState(event.getState());
        }
        return eventFromDto;
    }
}
