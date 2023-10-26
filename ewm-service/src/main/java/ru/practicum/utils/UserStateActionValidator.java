package ru.practicum.utils;


import ru.practicum.event.UserStateAction;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserStateActionValidator implements ConstraintValidator<UserStateActionConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return UserStateAction.from(value).isPresent();
    }
}
