package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.VeiwStatsDto;
import ru.practicum.service.StatsService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHitDto saveStatHit(@RequestBody EndpointHitDto endpointHit) {
        log.info("В метод saveStatHit передан объект с полями: app {}, uri {}, ip {}, timestamp {}",
                endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp(), endpointHit.getTimestamp());
        return statsService.saveStatsHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<VeiwStatsDto> getVeiwStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam List<String> uris,
                                           @RequestParam(defaultValue = "false") boolean unique) {
        log.info("В метод getVeiwStats переданы параметры запроса: start {}, end {}, uri {}, unique {}",
                start, end, uris, unique);
        return statsService.getVeiwStats(start, end, uris, unique);
    }
}
