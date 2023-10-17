package ru.practicum.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.VeiwStatsDto;

import java.util.List;

public interface StatsService {
    EndpointHitDto saveStatsHit(EndpointHitDto endpointHit);

    List<VeiwStatsDto> getVeiwStats(String start, String end, List<String> uris, boolean unique);
}
