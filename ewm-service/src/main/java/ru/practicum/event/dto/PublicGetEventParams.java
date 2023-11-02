package ru.practicum.event.dto;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.utils.validations.EventSortConstrain;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Constants.DATE_FORMAT;
import static ru.practicum.utils.Constants.DATE_TIME_PATTERN;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PublicGetEventParams implements Params {

    private String text;
    private List<Long> categories;
    private Boolean paid;

    @Pattern(regexp = DATE_TIME_PATTERN, message = "Формат даты и времени должен соответствовать виду: 'yyyy-MM-dd HH:mm:ss'")
    private String rangeStart;

    @Pattern(regexp = DATE_TIME_PATTERN, message = "Формат даты и времени должен соответствовать виду: 'yyyy-MM-dd HH:mm:ss'")
    private String rangeEnd;

    private boolean onlyAvailable;

    @EventSortConstrain
    private String sort = "EVENT_DATE";

    @PositiveOrZero(message = "Параметр from не может быть отрицательным")
    private int from = 0;

    @Positive(message = "Параметр size не может быть отрицательным или ноль")
    private int size = 10;

    @Override
    public LocalDateTime getStartDateTime() {
        return !StringUtils.isEmpty(rangeStart) ? LocalDateTime.parse(rangeStart, DATE_FORMAT) : null;
    }

    @Override
    public LocalDateTime getEndDateTime() {
        return !StringUtils.isEmpty(rangeEnd) ? LocalDateTime.parse(rangeEnd, DATE_FORMAT) : null;
    }

}
