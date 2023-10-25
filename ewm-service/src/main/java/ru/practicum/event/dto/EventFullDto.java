package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EventFullDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private String state;
    private String title;
    private Long views;
}
