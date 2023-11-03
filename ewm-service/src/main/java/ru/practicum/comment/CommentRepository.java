package ru.practicum.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAuthor_IdAndEvent_Id(long userId, long eventId);

    Page<Comment> findAllByAuthor_Id(long userId, Pageable page);

    Page<Comment> findAllByEvent_Id(long eventId, Pageable page);

    @Query("select c.event.id as eventId, count(c.id) as commentsQuantity from Comment c where c.event.id in ?1 group by c.event.id")
    List<Map<String, Long>> countCommentsForEventIdIn(List<Long> eventsIds);
}
