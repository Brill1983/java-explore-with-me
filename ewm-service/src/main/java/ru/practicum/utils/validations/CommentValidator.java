package ru.practicum.utils.validations;

import ru.practicum.comment.CommentSort;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommentValidator implements ConstraintValidator<CommentSortConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return CommentSort.from(value).isPresent();
    }
}
