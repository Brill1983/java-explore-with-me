package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long userId);

    Optional<Request> findByRequesterIdAndEventId(long userId, long eventId);

    Integer countAllByStatusAndEventId(Status status, long eventId);

    List<Request> findAllByStatusAndEventIdIn(Status status, List<Long> eventsIds);

    List<Request> findAllByEventId(long eventId);

    @Modifying
    @Query("update Request as r set r.status = ?1 where r.id in ?2")
    void requestStatusUpdate(Status status, List<Long> id);

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByEventIdAndStatus(Long eventId, Status status);
}
