package com.example.taskday.domain.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;

import java.time.LocalDate;
import java.time.Period;

public class MinAgeValidator implements ConstraintValidator <MinAge, LocalDate> {
    private int minAge;

    @Override
    public void initialize(MinAge constraintAnnotation) {
        this.minAge = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return true;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= minAge;
    }
}
