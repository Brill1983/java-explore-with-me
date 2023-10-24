package ru.practicum.event;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.VeiwStatsDto;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ElementNotFoundException;
import ru.practicum.request.RequestRepository;
import ru.practicum.request.Status;
import ru.practicum.request.model.Request;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final RequestRepository requestRepository;


    @Override
    public EventFullDto postEvent(long userId, NewEventDto eventDto) {
        return null;
    }

    @Override
    public List<EventShortDto> getCurrentUserEvents(long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));
        Pageable page = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findAllByInitiator_Id(userId, page).toList();

        return mapEventsToShortDtos(events);
    }

    private List<EventShortDto> mapEventsToShortDtos(List<Event> events) { // TODO доделать Request
        Optional<LocalDateTime> start = events.stream() // получаем самую ранюю дату публикации
                .map(Event::getPublishedOn)
                .min(LocalDateTime::compareTo);

        List<Long> eventsIds = events.stream() // Формируем список с ID мероприятий для передачи в запрос requestRepository на получение списка запрсоов
                .map(Event::getId)
                .collect(Collectors.toList());

        List<String> uries = eventsIds.stream() // Формируем список URL для передачи в запрос getStats сервера статистики
                .map(id -> "/event/" + id)
                .collect(Collectors.toList());

        List<EventShortDto> eventShortDtoList = new ArrayList<>(); // Объявляем список EventShortDto для возврата из метода
//        List<Request> requestList = new ArrayList<>();

        if (start.isPresent()) { // если у событий из списка есть хотя бы одна дата публикации, то:
            // запрашиваем сервер статистики через getStats на получение списка ДТО уникальных просмотров
            // TODO отсюда можно в отдельный метод
            List<VeiwStatsDto> veiwStatsDtoList = statsClient.getStats(start.get(), LocalDateTime.now(), uries, true);

            Map<Long, Long> veiws = new HashMap<>(); // Объявляем Мапу <eventId, кол-во_просмотров>

            for (VeiwStatsDto veiwStatsDto : veiwStatsDtoList) { // проходим по списку ДТО просмотров
                String uri = veiwStatsDto.getUri(); // получаем строку URI из ДТО просмотра
                Long eventId = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1)); // отрезаем от строки номер события и преобразуем а Long
                veiws.put(eventId, veiwStatsDto.getHits()); // заполняем мапу
            }
            // TODO до сюда
            // TODO отсюда можно в отдельный метод
            List<Request> requestList = requestRepository.findAllByStatusAndEvent_IdIn(Status.CONFIRMED, eventsIds);
            Map<Long, Integer> eventsRequests = new HashMap<>();
            for (Request request : requestList) {
                if (eventsRequests.containsKey(request.getId())) {
                    Integer count = eventsRequests.get(request.getId());
                    eventsRequests.put(request.getId(), ++count);
                } else {
                    eventsRequests.put(request.getId(), 1);
                }
            }
            // TODO до сюда
            for (Event event : events) { // мапим список Событий на ДТО, заполняем поле запросов и просмотров, если просмотров не было - то 0
                eventShortDtoList.add(
                        EventMapper.toShortDto(event, eventsRequests.getOrDefault(event.getId(), 0), veiws.getOrDefault(event.getId(), 0L))
                );
            }

        } else { // если у событий из списка не было публикаций - то просмотры везде 0
            for (Event event : events) {
                eventShortDtoList.add(EventMapper.toShortDto(event, 0, 0L));
            }
        }

        return eventShortDtoList;
    }
}
