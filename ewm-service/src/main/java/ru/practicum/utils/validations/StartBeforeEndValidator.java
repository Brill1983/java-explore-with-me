package ru.practicum.utils.validations;

import ru.practicum.event.dto.Params;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEndConstrain, Params> {

    @Override
    public boolean isValid(Params value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime start = value.getStartDateTime();
        LocalDateTime end = value.getEndDateTime();

        return (end == null || start == null) || !end.isBefore(start);
    }
}
