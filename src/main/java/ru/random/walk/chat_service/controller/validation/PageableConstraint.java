package ru.random.walk.chat_service.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.random.walk.chat_service.controller.validation.impl.PageableRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PageableRangeValidator.class)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageableConstraint {
    String message() default "Invalid pagination parameters!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int maxPageSize() default Integer.MAX_VALUE;
}
