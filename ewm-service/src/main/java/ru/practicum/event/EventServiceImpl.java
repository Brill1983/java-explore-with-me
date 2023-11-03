package ru.practicum.event;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.VeiwStatsDto;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.comment.CommentRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ElementNotFoundException;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;
import ru.practicum.location.model.Location;
import ru.practicum.request.RequestMapper;
import ru.practicum.request.RequestRepository;
import ru.practicum.request.Status;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.utils.Constants.APP_NAME;
import static ru.practicum.utils.Constants.DATE_FORMAT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;

    //________________________________Private Part_____________________________________________________________________

    @Override
    public List<EventShortDto> getCurrentUserEvents(long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Pageable page = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findAllByInitiator_Id(userId, page).toList();

        return mapEventsToShortDtos(events);
    }

    @Transactional
    @Override
    public EventFullDto postEvent(long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new ElementNotFoundException("Категория с ID: " + eventDto.getCategory() + " не найден"));

        Location location = locationRepository.save(LocationMapper.toModel(eventDto.getLocation()));

        Event event = eventRepository.save(EventMapper.toEvent(eventDto, user, category, location));
        return EventMapper.toFullDto(event, 0, 0L);
    }

    @Override
    public EventFullDto getOwnerEvent(long userId, long eventId) {

        Event event = checkUserAndEventId(userId, eventId);
        checkEventInitiator(event, userId);

        return mapEventsToFullDtos(List.of(event)).get(0);
    }

    @Transactional
    @Override
    public EventFullDto patchCurrentUserEvent(long userId, long eventId, UpdateEventUserRequest eventDto) {

        Event event = checkUserAndEventId(userId, eventId);
        checkEventInitiator(event, userId);

        Category category = null;
        if (eventDto.getCategory() != null) {
            category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new ElementNotFoundException("Категория с ID: " + eventDto.getCategory() + " не найдена"));
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Изменять можно только отмененные или еще не опубликованные события");
        }
        Location location = null;
        if (eventDto.getLocation() != null) {
            location = locationRepository.save(LocationMapper.toModel(eventDto.getLocation()));
        }

        Event eventFromDb = eventRepository.save(EventMapper.toEventFromUserUpdateDto(event, eventDto, category, location));
        return mapEventsToFullDtos(List.of(eventFromDb)).get(0);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForOwnersEvent(long userId, long eventId) {

        Event event = checkUserAndEventId(userId, eventId);
        checkEventInitiator(event, userId);

        return requestRepository.findAllByEvent_Id(eventId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult patchRequestsForOwnersEvent(long userId, long eventId,
                                                                      EventRequestStatusUpdateRequest updateRequest) {
        Event event = checkUserAndEventId(userId, eventId);
        checkEventInitiator(event, userId);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return mapRequestsAndFormResult(eventId);
        }

        return updateRequestStatusAndMapResult(event, updateRequest);
    }

    //________________________________ Admin Part_____________________________________________________________________

    @Override
    public List<EventFullDto> getAdminFullEvent(AdminGetEventParams params) {

        LocalDateTime start = params.getStartDateTime();
        LocalDateTime end = params.getEndDateTime();

        Pageable page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());

        BooleanBuilder query = new BooleanBuilder()
                .and(!CollectionUtils.isEmpty(params.getUsers()) ? QEvent.event.initiator.id.in(params.getUsers()) : null)
                .and(!CollectionUtils.isEmpty(params.getCategories()) ? QEvent.event.category.id.in(params.getCategories()) : null)
                .and(start != null ? QEvent.event.eventDate.goe(start) : null)
                .and(end != null ? QEvent.event.eventDate.loe(end) : null);

        if (!CollectionUtils.isEmpty(params.getStates())) {
            List<State> stateList = params.getStates().stream()
                    .map(State::valueOf)
                    .collect(Collectors.toList());
            query.and(QEvent.event.state.in(stateList));
        }
        Page<Event> events = eventRepository.findAll(query, page);

        return mapEventsToFullDtos(events.toList());
    }

    @Transactional
    @Override
    public EventFullDto patchAdminEvent(long eventId, UpdateEventAdminRequest eventDto) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException("События с ID: " + eventId + " не найдено"));
        Category category = null;
        if (eventDto.getCategory() != null) {
            category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new ElementNotFoundException("Категория с ID: " + eventDto.getCategory() + " не найдена"));
        }

        if (event.getPublishedOn() != null && event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Изменять событие можно только не позднее чем за час до его начала");
        }

        if (eventDto.getStateAction() != null) {
            if (AdminStateAction.valueOf(eventDto.getStateAction()).equals(AdminStateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(State.PENDING)) {
                    throw new ConflictException("Опубликовать можно только событие имеющее статус PENDING");
                }
            } else {
                if (!event.getState().equals(State.PENDING)) {
                    throw new ConflictException("Событие можно отклонить, только если оно еще не опубликовано");
                }
            }
        }

        Location location = null;
        if (eventDto.getLocation() != null) {
            location = locationRepository.save(LocationMapper.toModel(eventDto.getLocation()));
        }

        Event eventFromDb = eventRepository.save(EventMapper.toEventFromAdminUpdateDto(event, eventDto, category, location));

        return mapEventsToFullDtos(List.of(eventFromDb)).get(0);
    }

    //________________________________ Public Part_____________________________________________________________________

    @Override
    public List<EventShortDto> getPublicEvents(PublicGetEventParams params, HttpServletRequest request) {

        LocalDateTime start = params.getStartDateTime();
        LocalDateTime end = params.getEndDateTime();

        Pageable page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());

        BooleanBuilder query = new BooleanBuilder()
                .and(params.getText() != null ? QEvent.event.annotation.containsIgnoreCase(params.getText()) : null)
                .or(params.getText() != null ? QEvent.event.description.containsIgnoreCase(params.getText()) : null)
                .and(QEvent.event.state.eq(State.PUBLISHED))
                .and(!CollectionUtils.isEmpty(params.getCategories()) ? QEvent.event.category.id.in(params.getCategories()) : null)
                .and(params.getPaid() != null ? QEvent.event.paid.eq(params.getPaid()) : null)
                .and((end == null && start == null) ?
                        QEvent.event.eventDate.after(LocalDateTime.now()) :
                        QEvent.event.eventDate.between(start, end));

        if (params.isOnlyAvailable()) {
            query.and(QEvent.event.participantLimit.goe(0));
        }

        Page<Event> events = eventRepository.findAll(query, page);

        List<EventShortDto> shortDtoList = mapEventsToShortDtos(events.toList());

        if (Sort.valueOf(params.getSort().toUpperCase()).equals(Sort.EVENT_DATE)) {
            shortDtoList.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        } else {
            shortDtoList.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        EndpointHitDto hitDto = new EndpointHitDto(null, APP_NAME, request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().format(DATE_FORMAT));
        statsClient.postHit(hitDto);

        return shortDtoList;
    }

    @Override
    public EventFullDto getPublicEventById(long eventId, HttpServletRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException("События с ID: " + eventId + " не найдено"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ElementNotFoundException("События с ID: " + eventId + " не найдено среди опубликованных");
        }

        EventFullDto eventFullDto = mapEventsToFullDtos(List.of(event)).get(0);

        EndpointHitDto hitDto = new EndpointHitDto(null, APP_NAME, request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().format(DATE_FORMAT));
        statsClient.postHit(hitDto);

        return eventFullDto;
    }

    //________________________________ Util Part_____________________________________________________________________

    @Override
    public List<EventShortDto> mapEventsToShortDtos(List<Event> events) {

        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Optional<LocalDateTime> start = eventRepository.getMinPublishedDate(eventsIds);

        Map<Long, Long> commentsQuantity = getCommentCountToEvents(eventsIds);

        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (start.isPresent()) {
            Map<Long, Long> veiws = getStatsForEvents(start.get(), eventsIds);
            Map<Long, Integer> eventsRequests = getEventRequests(Status.CONFIRMED, eventsIds);
            for (Event event : events) {
                eventShortDtoList.add(
                        EventMapper.toShortDto(event,
                                eventsRequests.getOrDefault(event.getId(), 0),
                                veiws.getOrDefault(event.getId(), 0L),
                                commentsQuantity.getOrDefault(event.getId(), 0L))
                );
            }
        } else {
            for (Event event : events) {
                eventShortDtoList.add(EventMapper.toShortDto(event, 0, 0L, 0L));
            }
        }

        return eventShortDtoList;
    }

    private Map<Long, Long> getCommentCountToEvents(List<Long> eventsIds) {

        Map<Long, Long> eventCommentsCount = new HashMap<>();

        List<Map<String, Long>> commentsQuantity = commentRepository.countCommentsForEventIdIn(eventsIds);

        for (Map<String, Long> longLongMap : commentsQuantity) {
            eventCommentsCount.put(longLongMap.get("eventId"), longLongMap.get("commentsQuantity"));
        }
        return eventCommentsCount;
    }

    private List<EventFullDto> mapEventsToFullDtos(List<Event> events) {

        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Optional<LocalDateTime> start = eventRepository.getMinPublishedDate(eventsIds);

        List<EventFullDto> eventShortDtoList = new ArrayList<>();
        if (start.isPresent()) {
            Map<Long, Long> veiws = getStatsForEvents(start.get(), eventsIds);
            Map<Long, Integer> eventsRequests = getEventRequests(Status.CONFIRMED, eventsIds);
            for (Event event : events) {
                eventShortDtoList.add(
                        EventMapper.toFullDto(event, eventsRequests.getOrDefault(event.getId(), 0), veiws.getOrDefault(event.getId(), 0L))
                );
            }
        } else {
            for (Event event : events) {
                eventShortDtoList.add(EventMapper.toFullDto(event, 0, 0L));
            }
        }

        return eventShortDtoList;
    }

    private Map<Long, Long> getStatsForEvents(LocalDateTime start, List<Long> eventsIds) {

        List<String> uries = eventsIds.stream()
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());

        List<VeiwStatsDto> veiwStatsDtoList = statsClient.getStats(start, LocalDateTime.now(), uries, true);

        Map<Long, Long> veiws = new HashMap<>();

        for (VeiwStatsDto veiwStatsDto : veiwStatsDtoList) {
            String uri = veiwStatsDto.getUri();
            Long eventId = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            veiws.put(eventId, veiwStatsDto.getHits());
        }
        return veiws;
    }

    private Map<Long, Integer> getEventRequests(Status status, List<Long> eventsIds) {
        List<Request> requestList = requestRepository.findAllByStatusAndEvent_IdIn(status, eventsIds);
        Map<Long, Integer> eventsRequests = new HashMap<>();
        for (Request request : requestList) {
            if (eventsRequests.containsKey(request.getId())) {
                Integer count = eventsRequests.get(request.getId());
                eventsRequests.put(request.getEvent().getId(), ++count);
            } else {
                eventsRequests.put(request.getEvent().getId(), 1);
            }
        }
        return eventsRequests;
    }

    private EventRequestStatusUpdateResult mapRequestsAndFormResult(long eventId) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        List<Request> requestsList = requestRepository.findAllByEvent_Id(eventId);
        for (Request request : requestsList) {
            if (request.getStatus().equals(Status.REJECTED)) {
                rejectedRequests.add(RequestMapper.toDto(request));
            } else if (request.getStatus().equals(Status.CONFIRMED)) {
                confirmedRequests.add(RequestMapper.toDto(request));
            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private EventRequestStatusUpdateResult updateRequestStatusAndMapResult(Event event, EventRequestStatusUpdateRequest updateRequest) {
        Integer confirmRequests = requestRepository.countAllByStatusAndEvent_Id(Status.CONFIRMED, event.getId());
        if (event.getParticipantLimit() <= confirmRequests) {
            throw new ConflictException("На событие с ID: " + event.getId() +
                    " уже зарегистрировано максимально кол-во участников: " + confirmRequests);
        }

        List<Request> requestList = requestRepository.findAllByIdIn(updateRequest.getRequestIds());

        int counter = 0;
        int requestsToUpdate = event.getParticipantLimit() - confirmRequests;
        List<Long> requestsIdsForConfirm = new ArrayList<>(requestsToUpdate);
        List<Long> requestsIdsForReject = new ArrayList<>(confirmRequests);

        List<Request> confirmedRequestList = requestRepository.findAllByEvent_IdAndStatus(event.getId(), Status.CONFIRMED);
        List<Request> rejectRequestList = requestRepository.findAllByEvent_IdAndStatus(event.getId(), Status.REJECTED);

        for (Request request : requestList) {
            if (!request.getStatus().equals(Status.PENDING)) {
                throw new ConflictException("У запроса с ID: " + request.getId() + "статус: " + request.getStatus() +
                        ", ожидалось PENDING");
            }
            if (!Objects.equals(request.getEvent().getId(), event.getId())) {
                throw new ConflictException("Запрос с ID: " + request.getId() + "относится к событию с ID: "
                        + request.getEvent().getId() + ", а не к событию с " + event.getId());
            }
            if (updateRequest.getStatus().equals(Status.CONFIRMED.name()) && counter < requestsToUpdate) {
                requestsIdsForConfirm.add(request.getId());
                request.setStatus(Status.CONFIRMED);
                confirmedRequestList.add(request);
                counter++;
            } else {
                requestsIdsForReject.add(request.getId());
                request.setStatus(Status.REJECTED);
                rejectRequestList.add(request);
            }
        }
        requestRepository.requestStatusUpdate(Status.valueOf(updateRequest.getStatus()), requestsIdsForConfirm);
        if (!requestsIdsForReject.isEmpty()) {
            requestRepository.requestStatusUpdate(Status.REJECTED, requestsIdsForConfirm);
        }

        List<ParticipationRequestDto> confirmedRequests = confirmedRequestList.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());

        List<ParticipationRequestDto> rejectedRequests = rejectRequestList.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private Event checkUserAndEventId(long userId, long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException("События с ID: " + eventId + " не найдено"));
    }

    private void checkEventInitiator(Event event, long userId) {
        if (event.getInitiator().getId() != userId) {
            throw new ConflictException("Событие с ID: " + event.getId() + ", создано пользователем с ID: " +
                    event.getInitiator().getId() + ", а не пользователем с ID: " + userId);
        }
    }

}
