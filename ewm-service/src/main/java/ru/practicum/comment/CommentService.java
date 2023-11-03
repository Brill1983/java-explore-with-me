package ru.practicum.comment;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;

import java.util.List;

public interface CommentService {
    CommentShortDto postComment(long userId, long eventId, CommentDto commentDto);

    CommentShortDto patchComment(long userId, long commentId, CommentDto commentDto);

    List<CommentShortDto> getOwnerCommentsForEvent(long userId, long eventId);

    CommentFullDto getCommentById(long commentId);

    List<CommentShortDto> getUserComments(long userId, Integer from, Integer size, String sort);

    List<CommentFullDto> getCommentsByEventId(long eventId, Integer from, Integer size, String sort);

    void deleteOwnersCommentById(long userId, long commentId);

    void deleteCommentById(long commentId);
}
