package ru.practicum.utils.validations;

import ru.practicum.event.Sort;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventSortValidator implements ConstraintValidator<EventSortConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Sort.from(value).isPresent();
    }
}
