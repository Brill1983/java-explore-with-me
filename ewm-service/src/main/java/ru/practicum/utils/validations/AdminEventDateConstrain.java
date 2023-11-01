package ru.practicum.utils.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdminEventDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminEventDateConstrain {

    String message() default "Дата и время на которые намечено событие не может быть раньше, чем через час от текущего момента";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

