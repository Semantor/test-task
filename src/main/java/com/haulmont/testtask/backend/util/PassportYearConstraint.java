package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.settings.ErrorMessages;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassportYearConstraintValidator.class)
@Documented
public @interface PassportYearConstraint {
    String message() default ErrorMessages.PASSPORT_LENGTH_ERROR_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
