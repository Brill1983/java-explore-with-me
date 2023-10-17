package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsClient { //TODO сделать логирование

    private final RestTemplate rest;

    @Value("${stats-server.url}")
    private final String serverUrl;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ResponseEntity<Object> postHit(EndpointHitDto hit) {
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(hit);
        return rest.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, Object.class);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique){
        String startDayTime = start.format(formatter);
        String endDayTime = end.format(formatter);
        Map<String, Object> parameters;
        if(unique != null) {
            parameters = Map.of(
                    "start", startDayTime,
                    "end", endDayTime,
                    "uris", uris,
                    "unique", unique
            );
        } else {
            parameters = Map.of(
                    "start", startDayTime,
                    "end", endDayTime,
                    "uris", uris
            );
        }
        return rest.exchange(serverUrl + "/stats", HttpMethod.GET, null, Object.class, parameters);
    }
}
