package com.haulmont.testtask.backend;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class ValidatorTest {

    @Autowired
    private Validator validator;
    @Autowired
    private CreditConstraintProvider creditConstraintProvider;




    @Test
    void isNullOrBlankForNull() {
        assertTrue(validator.isNullOrBlank(null));
    }

    @Test
    void isNullOrBlankForEmpty() {
        assertTrue(validator.isNullOrBlank(""));
    }

    @Test
    void isNullOrBlankForBlank() {
        assertTrue(validator.isNullOrBlank("      "));
    }

    @Test
    void isNullOrBlank() {
        assertFalse(validator.isNullOrBlank("asdf"));
    }

    @Test
    void validateCreditLimitWith0() {
        BigDecimal input = BigDecimal.ZERO;
        assertFalse(validator.validateCreditAmount(input, creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWith10() {
        BigDecimal input = BigDecimal.TEN;
        assertFalse(validator.validateCreditAmount(input, creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMin() {
        BigDecimal input = creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE;
        assertTrue(validator.validateCreditAmount(input, creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMax() {
        BigDecimal input = creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE;
        assertTrue(validator.validateCreditAmount(input, creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithAVG() {
        BigDecimal input = creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE.add(creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
        assertTrue(validator.validateCreditAmount(input, creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMAXadd1() {
        BigDecimal input = creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE.add(BigDecimal.ONE);
        assertFalse(validator.validateCreditAmount(input, creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE));
    }


    @Test
    void validateCreditRateWithMinusOne() {
        BigDecimal bd = new BigDecimal(-1);
        assertFalse(validator.validateCreditRate(bd));
    }

    @Test
    void validateCreditRateWithCreditMinRateMinusOne() {
        BigDecimal bd = creditConstraintProvider.CREDIT_RATE_MIN_VALUE.subtract(BigDecimal.ONE);
        assertFalse(validator.validateCreditRate(bd));
    }

    @Test
    void validateCreditRateWithCreditMinRate() {
        BigDecimal bd = creditConstraintProvider.CREDIT_RATE_MIN_VALUE;
        assertTrue(validator.validateCreditRate(bd));
    }

    @Test
    void validateCreditRateWithCreditMaxRate() {
        BigDecimal bd = creditConstraintProvider.CREDIT_RATE_MAX_VALUE;
        assertTrue(validator.validateCreditRate(bd));
    }

    @Test
    void validateCreditRateWithCreditMaxRatePlusOne() {
        BigDecimal bd = creditConstraintProvider.CREDIT_RATE_MAX_VALUE.add(BigDecimal.ONE);
        assertFalse(validator.validateCreditRate(bd));
    }
}