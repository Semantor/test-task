package com.haulmont.testtask.backend.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.haulmont.testtask.settings.ErrorMessages.PASSPORT_LENGTH_ERROR_MSG;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassportYearConstraintValidator.class)
@Documented
public @interface PassportYearConstraint {
    String message() default PASSPORT_LENGTH_ERROR_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
