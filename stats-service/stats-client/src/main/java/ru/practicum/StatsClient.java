package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate rest;

    @Value("${stats-server.url}")
    private final String serverUrl;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void postHit(EndpointHitIn hit) {
        HttpEntity<EndpointHitIn> requestEntity = new HttpEntity<>(hit);
        rest.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, EndpointHitIn.class);
    }

    public VeiwStats getStats(String start, String end, String[] uris, Boolean unique){
        LocalDateTime startDayTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDayTime = LocalDateTime.parse(end, formatter);
        Map<String, Object> parameters = Map.of(
                "start", startDayTime,
                "end", endDayTime,
                "uris", uris
        );
        return rest.exchange(serverUrl + "/stats", HttpMethod.GET, null, VeiwStats.class, parameters).getBody();
    }
}
