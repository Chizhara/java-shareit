package ru.practicum.shareit.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IntervalValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntervalConstraint {
    String message() default "Invalid LocalDateTime time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}