package ru.practicum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.VeiwStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.VeiwStatsDto(e.app, e.uri, count(e.id)) from EndpointHit as e where e.timestamp > ?1 and e.timestamp < ?2 and e.uri in ?3")
    List<VeiwStatsDto> getStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris);

    @Query("select new ru.practicum.VeiwStatsDto(e.app, e.uri, count(e.id)) from EndpointHit as e where e.timestamp > ?1 and e.timestamp < ?2 and e.uri in ?3 group by e.ip")
    List<VeiwStatsDto> getUniqueStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris);
}
