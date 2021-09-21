package ru.vtb.stub.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class MethodValidator implements ConstraintValidator<Method, String> {

    @Override
    public void initialize(Method constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            HttpMethod.valueOf(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
