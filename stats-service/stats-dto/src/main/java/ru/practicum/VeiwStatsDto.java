package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
//@AllArgsConstructor
public class VeiwStatsDto {
    private String app;

    private String uri;

    private Long hits;

    public VeiwStatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
