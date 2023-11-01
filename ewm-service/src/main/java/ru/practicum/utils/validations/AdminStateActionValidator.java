package ru.practicum.utils.validations;

import ru.practicum.event.AdminStateAction;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AdminStateActionValidator implements ConstraintValidator<AdminStateActionConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return AdminStateAction.from(value).isPresent();
    }
}
