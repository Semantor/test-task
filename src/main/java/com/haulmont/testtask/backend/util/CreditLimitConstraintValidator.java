package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.backend.CreditConstraintProvider;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreditLimitConstraintValidator implements ConstraintValidator<CreditLimitConstraint, BigDecimal> {
    private final CreditConstraintProvider creditConstraintProvider;

    @Override
    public void initialize(CreditLimitConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.compareTo(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE) > -1 &&
                value.compareTo(creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE) < 1;
    }
}
