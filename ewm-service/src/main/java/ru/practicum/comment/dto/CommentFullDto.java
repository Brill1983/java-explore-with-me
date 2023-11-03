package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentFullDto {

    private Long id;

    private String text;

    private UserShortDto author;

    private EventShortDto event;

    private LocalDateTime createdOn;
}
