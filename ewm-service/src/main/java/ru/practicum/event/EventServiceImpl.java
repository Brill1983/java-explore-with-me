package ru.practicum.event;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.ElementNotFoundException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserService;
import ru.practicum.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Override
    public EventShortDto getCurrentUserEvents(long userId, Integer from, Integer size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь с ID: " + userId + " не найден"));

        Pageable page = PageRequest.of(from / size, size);

        Page<Event> event = eventRepository.findAllByInitiator_Id(userId, page);
        return null;
    }

    @Override
    public EventFullDto postEvent(long userId, NewEventDto eventDto) {
        return null;
    }
}
