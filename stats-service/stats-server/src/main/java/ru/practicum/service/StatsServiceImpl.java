package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.practicum.EndpointHitDto;
import ru.practicum.VeiwStatsDto;
import ru.practicum.dao.StatsRepository;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public EndpointHitDto saveStatsHit(EndpointHitDto endpointHit) {
        EndpointHit hit = statsRepository.save(EndpointHitMapper.toModel(endpointHit));
        log.info("В базу сохранен объект hits: id {}, app {}, uri {}, ip {}, timestamp {}",
                hit.getId(), hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
        return EndpointHitMapper.toDto(hit);
    }

    @Override
    public List<VeiwStatsDto> getVeiwStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique) {

        List<VeiwStatsDto> veiwStatsDtoList = unique ?
                CollectionUtils.isEmpty(uris) ?
                        statsRepository.getUniqueStatsWithoutUris(start, end) :
                        statsRepository.getUniqueStats(start, end, uris) :
                CollectionUtils.isEmpty(uris) ?
                        statsRepository.getStatsWithoutUris(start, end) :
                        statsRepository.getStats(start, end, uris);

        log.info("Из базы получен список из {} элементов", veiwStatsDtoList.size());
        return veiwStatsDtoList;
    }
}
