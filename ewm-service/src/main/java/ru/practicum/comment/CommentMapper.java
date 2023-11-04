package ru.practicum.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public CommentShortDto toShortDto(Comment comment, EventShortDto eventDto) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(eventDto)
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public Comment toComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public CommentFullDto toFullDto(Comment comment, UserShortDto userDto, EventShortDto eventDto) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(userDto)
                .event(eventDto)
                .createdOn(comment.getCreatedOn())
                .build();
    }
}
