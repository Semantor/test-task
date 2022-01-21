package com.haulmont.testtask.backend.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.haulmont.testtask.settings.ErrorMessages.*;

public final class ConstraintViolationHandler<T> {
    private ConstraintViolationHandler() {
    }

    public static <T> String handleToString(Set<ConstraintViolation<T>> constraintViolations) {
        if (constraintViolations.isEmpty()) return NO_ERRORS;
        StringBuilder result = new StringBuilder(ENUMERATION_ERRORS);
        constraintViolations.forEach(
                objectConstraintViolation ->
                        result.append(objectConstraintViolation.getMessage()).append(ERROR_TEXT_DELIMITER)
        );
        return result.toString();
    }
}