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
        Optional<LocalDateTime> start = events.stream()
                .map(Event::getPublishedOn)
                .min(LocalDateTime::compareTo);

        List<String> uries = events.stream()
                .map(Event::getId)
                .map(id -> "/event/" + id)
                .collect(Collectors.toList());

        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (start.isPresent()) {
            List<VeiwStatsDto> veiwStatsDtoList = statsClient.getStats(start.get(), LocalDateTime.now(), uries, true);

            Map<Long, Long> veiws = new HashMap<>();
            for (VeiwStatsDto veiwStatsDto : veiwStatsDtoList) {
                String uri = veiwStatsDto.getUri();
                Long eventId = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
                veiws.put(eventId, veiwStatsDto.getHits());
            }
            for (Event event : events) {
                eventShortDtoList.add(
                        EventMapper.toShortDto(event, null, veiws.getOrDefault(event.getId(), 0L))
                );
            }

        } else {
            for (Event event : events) {
                eventShortDtoList.add(EventMapper.toShortDto(event, null, 0L));
            }
        }

        return eventShortDtoList;
    }
}
