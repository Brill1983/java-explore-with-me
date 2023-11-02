package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.utils.validations.CommentSortConstrain;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Validated
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentFullDto getCommentById(@PathVariable long commentId) {
        log.info("В метод getCommentById переданы данные: commentId = {}", commentId);
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/events/{eventId}")
    public List<CommentFullDto> getCommentsByEventId(@PathVariable long eventId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size,
                                                     @RequestParam(defaultValue = "desc") @CommentSortConstrain String sort) {
        log.info("В метод getCommentsByEventId переданы данные: eventId = {}, from = {}, size = {}, sort = {}",
                eventId, from, size, sort);
        return commentService.getCommentsByEventId(eventId, from, size, sort);
    }


    @GetMapping("/users/{userId}")
    public List<CommentShortDto> getUserComments(@PathVariable long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size,
                                                 @RequestParam(defaultValue = "desc") @CommentSortConstrain String sort) {
        log.info("В метод getUserComments переданы данные: userId = {}, from = {}, size = {}, sort = {}",
                userId, from, size, sort);
        return commentService.getUserComments(userId, from, size, sort);
    }

}
