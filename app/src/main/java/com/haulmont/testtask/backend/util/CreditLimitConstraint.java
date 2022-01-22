package com.haulmont.testtask.backend.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.haulmont.testtask.settings.ErrorMessages.WRONG_CREDIT_LIMIT_MESSAGE;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreditLimitConstraintValidator.class)
@Documented
public @interface CreditLimitConstraint {
    String message() default WRONG_CREDIT_LIMIT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
