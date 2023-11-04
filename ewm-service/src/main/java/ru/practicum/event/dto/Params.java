package ru.practicum.event.dto;

import java.time.LocalDateTime;

public interface Params {

    LocalDateTime getStartDateTime();

    LocalDateTime getEndDateTime();
}
