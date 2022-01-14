package com.haulmont.testtask.backend.util;

import com.haulmont.testtask.backend.CreditConstraintProvider;
import com.haulmont.testtask.model.entity.CreditOffer;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class CreditOfferLimitConstraintValidator implements ConstraintValidator<CreditOfferLimitConstraint, CreditOffer> {
    private final CreditConstraintProvider creditConstraintProvider;

    @Override
    public void initialize(CreditOfferLimitConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreditOffer value, ConstraintValidatorContext context) {
        if (value.getCreditAmount() == null) return false;
        return value.getCreditAmount().compareTo(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE) > -1
                && value.getCreditAmount().compareTo(value.getCredit().getCreditLimit()) < 1;
    }
}
