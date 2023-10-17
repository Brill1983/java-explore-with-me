package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.VeiwStatsDto;
import ru.practicum.dao.StatsRepository;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constants.Constants.FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{

    private final StatsRepository statsRepository;

    @Override
    public EndpointHitDto saveStatsHit(EndpointHitDto endpointHit) {
        EndpointHit hit = statsRepository.save(EndpointHitMapper.toModel(endpointHit));
        log.info("В базу сохранен объект hits: id {}, app {}, uri {}, ip {}, timestamp {}",
                hit.getId(), hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
        return EndpointHitMapper.toDto(hit);
    }

    @Override
    public List<VeiwStatsDto> getVeiwStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, FORMAT);
        LocalDateTime endTime = LocalDateTime.parse(end, FORMAT);
        if (unique) {
            List<VeiwStatsDto> veiwStatsDtoList = statsRepository.getUniqueStats(startTime, endTime, uris);
            log.info("Из базы получен список из {} элементов", veiwStatsDtoList.size());
            return veiwStatsDtoList;
        } else {
            List<VeiwStatsDto> veiwStatsDtoList = statsRepository.getStats(startTime, endTime, uris);
            log.info("Из базы получен список из {} элементов", veiwStatsDtoList.size());
            return veiwStatsDtoList;
        }
    }
}
