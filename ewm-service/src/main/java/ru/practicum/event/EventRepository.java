package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiator_Id(Long userId, Pageable page);
}
