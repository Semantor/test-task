package com.haulmont.testtask.backend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class Validator {
    private final CreditConstraintProvider creditConstraintProvider;

    public final boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    public boolean validateCreditAmount(BigDecimal verifiableCreditAmount) {
        return validateCreditAmount(verifiableCreditAmount, creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE);
    }

    public boolean validateCreditAmount(BigDecimal verifiableCreditAmount, BigDecimal creditLimit) {
        return verifiableCreditAmount.compareTo(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE) > -1
                && verifiableCreditAmount.compareTo(creditLimit) < 1;
    }

    public boolean validateCreditRate(BigDecimal verifiableCreditRate) {
        return verifiableCreditRate.compareTo(creditConstraintProvider.CREDIT_RATE_MAX_VALUE) < 1 &&
                verifiableCreditRate.compareTo(creditConstraintProvider.CREDIT_RATE_MIN_VALUE) > -1;
    }

}
