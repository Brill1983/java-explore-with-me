package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate = new RestTemplate();

//    @Value("${stats-server.url}")
    private final String serverUrl = "http://localhost:9090";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHitDto postHit(EndpointHitDto hit) {
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(hit);
        return restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, EndpointHitDto.class).getBody();
    }

    public List<VeiwStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> urisList, Boolean unique) {
        String startDayTime = start.format(formatter);
        String endDayTime = end.format(formatter);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(serverUrl)
                .path("/stats")
                .queryParam("start", startDayTime)
                .queryParam("end", endDayTime)
                .queryParam("uris", urisList)
                .queryParam("unique", unique);

        URI uriString = uriComponentsBuilder.build().toUri();

        VeiwStatsDto[] response = restTemplate.getForObject(uriString, VeiwStatsDto[].class);

        return response != null ? List.of(response) : Collections.emptyList();
    }
}
