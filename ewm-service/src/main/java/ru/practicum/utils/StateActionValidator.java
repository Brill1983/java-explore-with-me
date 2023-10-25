package ru.practicum.utils;

import ru.practicum.event.StateAction;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StateActionValidator implements ConstraintValidator<StateActionConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return StateAction.from(value).isPresent();
    }
}
