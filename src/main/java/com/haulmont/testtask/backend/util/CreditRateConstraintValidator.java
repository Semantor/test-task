package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.backend.CreditConstraintProvider;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

import static com.haulmont.testtask.settings.ErrorMessages.AND_LESS_THEN;
import static com.haulmont.testtask.settings.ErrorMessages.WRONG_RATE_MUST_BE_MORE_OR_EQUAL;

@RequiredArgsConstructor
public class CreditRateConstraintValidator implements ConstraintValidator<CreditRateConstraint, BigDecimal> {
    private final CreditConstraintProvider creditConstraintProvider;

    @Override
    public void initialize(CreditRateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        boolean isValid = value != null &&
                value.compareTo(creditConstraintProvider.CREDIT_RATE_MAX_VALUE) < 1 &&
                value.compareTo(creditConstraintProvider.CREDIT_RATE_MIN_VALUE) > -1;
        if (!isValid) {
            hibernateContext.disableDefaultConstraintViolation();
            hibernateContext
                    .buildConstraintViolationWithTemplate(
                            WRONG_RATE_MUST_BE_MORE_OR_EQUAL +
                                    creditConstraintProvider.CREDIT_RATE_MIN_VALUE +
                                    AND_LESS_THEN +
                                    creditConstraintProvider.CREDIT_RATE_MAX_VALUE)
                    .addConstraintViolation();
        }
        return isValid;
    }
}
