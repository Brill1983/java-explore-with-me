package ru.practicum.utils.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SortConstrain {

    String message() default "В поле Sort объекта передано неверное значение";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
