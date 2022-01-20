package com.haulmont.testtask.backend.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.haulmont.testtask.settings.BusinessLogicSettings.MAX_YEAR_IN_PASSPORT;
import static com.haulmont.testtask.settings.BusinessLogicSettings.MIN_YEAR_IN_PASSPORT;
import static com.haulmont.testtask.settings.ComponentSettings.PASSPORT_NUMBER_START_INDEX;
import static com.haulmont.testtask.settings.ComponentSettings.PASSPORT_SERIES_YEAR_START_POSITION;

public class PassportYearConstraintValidator implements ConstraintValidator<PassportYearConstraint, String> {
    @Override
    public void initialize(PassportYearConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int year = Integer.parseInt(value.substring(PASSPORT_SERIES_YEAR_START_POSITION, PASSPORT_NUMBER_START_INDEX));
        return MIN_YEAR_IN_PASSPORT < year || year < MAX_YEAR_IN_PASSPORT;
    }
}
