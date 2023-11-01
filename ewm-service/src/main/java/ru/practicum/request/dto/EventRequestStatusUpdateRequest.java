package ru.practicum.request.dto;

import lombok.*;
import ru.practicum.utils.validations.StatusConstrain;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventRequestStatusUpdateRequest {

    @NotNull
    private List<Long> requestIds;

    @StatusConstrain
    @NotNull
    private String status;
}
