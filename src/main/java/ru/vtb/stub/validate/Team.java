package ru.vtb.stub.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = "^[a-z0-9_-]+$")

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface Team {
    String message() default "{javax.validation.constraints.Team.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
