package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester_Id(Long userId);

    Optional<Request> findByRequester_IdAndEvent_Id(long userId, long eventId);

    Long countAllByStatusAndEvent_Id(Status status, long eventId);

    List<Request> findAllByStatusAndEvent_IdIn(Status status, List<Long> eventsIds);
    
}
