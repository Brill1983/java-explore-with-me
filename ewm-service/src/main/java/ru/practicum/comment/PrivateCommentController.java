package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class PrivateCommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/{eventId}")
    public CommentShortDto postComment(@PathVariable long userId,
                                       @PathVariable long eventId,
                                       @RequestBody @Valid CommentDto commentDto) {
        log.info("В метод postComment переданы данные: userId = {}, eventId = {}, commentDto = {}", userId, eventId, commentDto);
        return commentService.postComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentShortDto patchComment(@PathVariable long userId,
                                        @PathVariable long commentId,
                                        @RequestBody @Valid CommentDto commentDto) {
        log.info("В метод patchComment переданы данные: userId = {}, commentId = {}, commentDto = {}",
                userId, commentId, commentDto);
        return commentService.patchComment(userId, commentId, commentDto);
    }

    @GetMapping("/events/{eventId}")
    public List<CommentShortDto> getOwnerCommentsForEvent(@PathVariable long userId,
                                                          @PathVariable long eventId) {
        log.info("В метод getOwnerCommentForEvent переданы данные: userId = {}, eventId = {}", userId, eventId);
        return commentService.getOwnerCommentsForEvent(userId, eventId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void deleteOwnersCommentById(@PathVariable long userId,
                                        @PathVariable long commentId) {
        log.info("В метод deleteOwnersCommentById переданы данные: userId = {}, commentId = {}", userId, commentId);
        commentService.deleteOwnersCommentById(userId, commentId);
    }
}
