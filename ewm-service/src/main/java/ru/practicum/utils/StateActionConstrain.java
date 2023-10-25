package ru.practicum.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StateActionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StateActionConstrain {
    String message() default "В поле StateAction объекта класса UpdateEventUserRequestDto передано не верное значение";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
