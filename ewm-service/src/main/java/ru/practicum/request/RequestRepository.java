package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester_Id(Long userId);

    Optional<Request> findByRequester_IdAndEvent_Id(long userId, long eventId);

    Integer countAllByStatusAndEvent_Id(Status status, long eventId);

    List<Request> findAllByStatusAndEvent_IdIn(Status status, List<Long> eventsIds);

    List<Request> findAllByEvent_Id(long eventId);

    @Modifying
    @Query("update Request as r set r.status = ?1 where r.id in ?2")
    void requestStatusUpdate(Status status, List<Long> id);

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByEvent_IdAndStatus(Long eventId, Status status);
}
