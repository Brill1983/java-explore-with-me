package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentShortDto {

    private Long id;

    private String text;

    private EventShortDto event;

    private LocalDateTime createdOn;
}
