package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.utils.AdminStateActionConstrain;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UpdateEventAdminRequest extends UpdateEvent {

    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01]) (([0,1][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]$",
            message = "Формат даты и времени должен соответствовать виду: 'yyyy-MM-dd HH:mm:ss'")
    private String eventDate;

    @AdminStateActionConstrain
    private String stateAction;

}

