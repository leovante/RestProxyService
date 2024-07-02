package ru.vtb.stub.validate;

import io.micronaut.http.HttpStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatusValidator implements ConstraintValidator<Status, Integer> {

    @Override
    public void initialize(Status constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        try {
            HttpStatus.valueOf(integer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
