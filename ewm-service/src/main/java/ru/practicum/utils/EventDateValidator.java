package ru.practicum.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_FORMAT;

public class EventDateValidator implements ConstraintValidator<EventDateConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime dateTime = LocalDateTime.parse(value, DATE_FORMAT);
        if (dateTime.isAfter(LocalDateTime.now().plusHours(2))) {
            return true;
        }
        return false;
    }
}
