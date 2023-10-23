package ru.practicum.location.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Location {

    @Min(value = 0)
    @Max(value = 90)
    private double lan;

    @Min(value = 0)
    @Max(value = 90)
    private double lon;
}
