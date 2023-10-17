package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EndpointHitDto {

    private long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
