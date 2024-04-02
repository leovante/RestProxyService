package ru.vtb.stub.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = StatusValidator.class)
public @interface Status {
    String message() default "Wrong status value!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
