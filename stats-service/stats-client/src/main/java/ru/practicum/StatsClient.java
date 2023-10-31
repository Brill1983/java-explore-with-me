package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
public class StatsClient {

    private final String serverUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

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
