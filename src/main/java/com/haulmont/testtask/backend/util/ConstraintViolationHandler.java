package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.Setting;

import javax.validation.ConstraintViolation;
import java.util.Set;

public final class ConstraintViolationHandler<T> {
    private ConstraintViolationHandler() {
    }

    public static <T> String handleToString(Set<ConstraintViolation<T>> constraintViolations) {
        if (constraintViolations.isEmpty()) return Setting.NO_ERRORS;
        StringBuilder result = new StringBuilder(Setting.ENUMERATION_ERRORS);
        constraintViolations.forEach(
                objectConstraintViolation ->
                        result.append(objectConstraintViolation.getMessage()).append(Setting.ERROR_TEXT_DELIMITER)
        );
        return result.toString();
    }
}