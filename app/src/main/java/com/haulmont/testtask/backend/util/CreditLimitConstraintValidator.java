package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.backend.CreditConstraintProvider;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

import static com.haulmont.testtask.settings.ErrorMessages.AND_LESS_THEN;
import static com.haulmont.testtask.settings.ErrorMessages.WRONG_AMOUNT_MUST_BE_MORE_OR_EQUAL;

@RequiredArgsConstructor
public class CreditLimitConstraintValidator implements ConstraintValidator<CreditLimitConstraint, BigDecimal> {
    private final CreditConstraintProvider creditConstraintProvider;


    @Override
    public void initialize(CreditLimitConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        boolean isValid = value != null && value.compareTo(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE) > -1 &&
                value.compareTo(creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE) < 1;
        if (!isValid) {
            hibernateContext.disableDefaultConstraintViolation();
            hibernateContext
                    .buildConstraintViolationWithTemplate(
                            WRONG_AMOUNT_MUST_BE_MORE_OR_EQUAL +
                                    creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE +
                                    AND_LESS_THEN +
                                    creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE)
                    .addConstraintViolation();
        }
        return isValid;
    }
}
