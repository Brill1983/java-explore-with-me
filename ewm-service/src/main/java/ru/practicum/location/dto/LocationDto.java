package ru.practicum.location.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LocationDto {

    @Min(value = 0)
    @Max(value = 90)
    @NotNull
    private double lan;

    @Min(value = 0)
    @Max(value = 90)
    @NotNull
    private double lon;
}
