package ru.practicum.request;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

import static ru.practicum.utils.Constants.DATE_FORMAT;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated() != null ? request.getCreated().format(DATE_FORMAT) : null,
                request.getEvent() != null ? request.getEvent().getId() : null,
                request.getRequester() != null ? request.getRequester().getId() : null,
                request.getStatus() != null ? request.getStatus().name() : null
        );
    }
}
