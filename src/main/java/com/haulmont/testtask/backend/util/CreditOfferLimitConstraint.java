package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.Setting;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreditOfferLimitConstraintValidator.class)
@Documented
public @interface CreditOfferLimitConstraint {
    String message() default Setting.WRONG_CREDIT_LIMIT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
