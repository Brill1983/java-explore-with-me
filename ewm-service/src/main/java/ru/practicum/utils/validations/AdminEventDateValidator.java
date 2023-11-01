package ru.practicum.utils.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_FORMAT;

public class AdminEventDateValidator implements ConstraintValidator<AdminEventDateConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime dateTime = LocalDateTime.parse(value, DATE_FORMAT);
        if (dateTime.isAfter(LocalDateTime.now().plusHours(1))) {
            return true;
        }
        return false;
    }
}
