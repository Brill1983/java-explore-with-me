package ru.practicum.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserStateActionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserStateActionConstrain {
    String message() default "В поле StateAction объекта класса UpdateEventUserRequest передано неверное значение";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
