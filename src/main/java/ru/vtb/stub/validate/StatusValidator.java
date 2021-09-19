package ru.vtb.stub.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
