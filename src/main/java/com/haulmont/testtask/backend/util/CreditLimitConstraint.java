package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.Setting;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreditLimitConstraintValidator.class)
@Documented
public @interface CreditLimitConstraint {
    String message() default Setting.WRONG_CREDIT_LIMIT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
