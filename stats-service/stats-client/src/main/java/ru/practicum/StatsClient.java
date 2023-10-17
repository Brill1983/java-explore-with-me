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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate rest;

    @Value("${stats-server.url}")
    private final String serverUrl;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHitDto postHit(EndpointHitDto hit) {
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(hit);
        return rest.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, EndpointHitDto.class).getBody();
    }

    public List<VeiwStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> urisList, Boolean unique){
        String startDayTime = start.format(formatter);
        String endDayTime = end.format(formatter);

        StringBuilder uris = new StringBuilder();
        for (int i = 0; i < urisList.size() - 1; i++) {
            uris.append(urisList.get(i));
            uris.append("&uris=");
        }
        uris.append(urisList.get(urisList.size()-1));
        String u = uris.toString();

//        String uris = String.join("&&uris==", urisList);

        Map<String, Object> parameters;
        if(unique != null) {
            parameters = Map.of(
                    "start", startDayTime,
                    "end", endDayTime,
                    "uris", u,
                    "unique", unique
            );
        } else {
            parameters = Map.of(
                    "start", startDayTime,
                    "end", endDayTime,
                    "uris", u,
                    "unique", false
            );
        }

        String url = serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

        List<VeiwStatsDto> response = rest.getForObject(serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}", List.class, parameters);

        return response;
    }
}
