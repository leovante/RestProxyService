package ru.vtb.stub.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Pattern(regexp = "^[a-zA-Z0-9.@%/_-]+$")

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface Path {
    String message() default "{javax.validation.constraints.Path.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
