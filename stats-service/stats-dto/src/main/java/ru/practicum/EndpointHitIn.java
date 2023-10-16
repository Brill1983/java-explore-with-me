package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitIn {

    private long id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;
}
