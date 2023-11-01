package ru.practicum;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EndpointHitDto {

    private Long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
