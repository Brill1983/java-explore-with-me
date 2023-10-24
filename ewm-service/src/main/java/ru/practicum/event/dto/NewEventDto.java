package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.location.dto.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NewEventDto {

    @NotBlank(message = "Краткое описание события обязательно для заполнения")
    @Size(min = 20, max = 2000, message = "Краткое описание должно содержать не менее 20 и не более 2000 симоволов")
    private String annotation;

    @NotNull
    @Positive
    private Long category;

    @NotBlank(message = "Полное описание события обязательно для заполнения")
    @Size(min = 20, max = 7000, message = "Полное описание описание должно содержать не менее 20 и не более 7000 симоволов")
    private String description;

    @Pattern(regexp = "^[0-9]{4}-(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9]) (([0,1][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]$",
    message = "Формат даты и времени должен соответствовать виду: 'yyyy-MM-dd HH:mm:ss'")
    private String eventDate;

    @NotNull
    @Valid
    private LocationDto locationDto;

    private boolean paid;

    @PositiveOrZero
    private int participantLimit;

    private boolean requestModeration;

    @Size(min = 3, max = 120, message = "Заголовок должн содержать не менее 3 и не более 120 симоволов")
    private String title;
}



