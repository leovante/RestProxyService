package ru.vtb.stub.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = "^[a-zA-Z0-9.@/_-]+$")

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface Path {
    String message() default "{javax.validation.constraints.Path.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
