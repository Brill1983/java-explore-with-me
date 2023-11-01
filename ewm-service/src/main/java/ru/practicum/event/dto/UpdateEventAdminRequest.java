package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.utils.validations.AdminEventDateConstrain;
import ru.practicum.utils.validations.AdminStateActionConstrain;

import javax.validation.constraints.Pattern;

import static ru.practicum.utils.Constants.DATE_TIME_PATTERN;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UpdateEventAdminRequest extends UpdateEvent {

    @Pattern(regexp = DATE_TIME_PATTERN,
            message = "Формат даты и времени должен соответствовать виду: 'yyyy-MM-dd HH:mm:ss'")
    @AdminEventDateConstrain
    private String eventDate;

    @AdminStateActionConstrain
    private String stateAction;

}

