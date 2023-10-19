package ru.practicum.service;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.util.Constants.DATE_FORMAT;

@UtilityClass
public class EndpointHitMapper {

    public EndpointHitDto toDto(EndpointHit hit) {
        return new EndpointHitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp() != null ? hit.getTimestamp().format(DATE_FORMAT) : null
        );
    }

    public EndpointHit toModel(EndpointHitDto hitDto) {
        return new EndpointHit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                StringUtils.isNotBlank(hitDto.getTimestamp()) ? LocalDateTime.parse(hitDto.getTimestamp(), DATE_FORMAT) : null
        );
    }
}
