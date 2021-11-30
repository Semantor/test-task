package com.haulmont.testtask.backend;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import org.junit.jupiter.api.Test;
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

    @Test
    void validateUUIDWithNullInput() {
        String input = null;
        assertNull(validator.validateUUID(input));
    }

    @Test
    void validateUUIDWithEmptyInput() {
        String input = "";
        assertNull(validator.validateUUID(input));
    }

    @Test
    void validateUUIDWithBlankInput() {
        String input = "   ";
        assertNull(validator.validateUUID(input));
    }

    @Test
    void validateUUIDWithRandomStringInput() {
        String input = "  asdefasd ";
        assertNull(validator.validateUUID(input));
    }

    @Test
    void validateUUIDWithCorrectInput() {
        String input = "c9457c4d-d778-4ef4-abd8-917212cddb75";
        UUID expected = UUID.fromString("c9457c4d-d778-4ef4-abd8-917212cddb75");
        assertEquals(expected, validator.validateUUID(input));
    }


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
        assertFalse(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWith10() {
        BigDecimal input = BigDecimal.TEN;
        assertFalse(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMin() {
        BigDecimal input = Config.CREDIT_LIMIT_MIN_VALUE;
        assertTrue(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMax() {
        BigDecimal input = Config.CREDIT_LIMIT_MAX_VALUE;
        assertTrue(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithAVG() {
        BigDecimal input = Config.CREDIT_LIMIT_MIN_VALUE.add(Config.CREDIT_LIMIT_MAX_VALUE).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
        assertTrue(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMAXadd1() {
        BigDecimal input = Config.CREDIT_LIMIT_MAX_VALUE.add(BigDecimal.ONE);
        assertFalse(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }


    @Test
    void validateCreditRateWithMinusOne() {
        BigDecimal bd = new BigDecimal(-1);
        assertFalse(validator.validateCreditRate(bd));
    }

    @Test
    void validateCreditRateWithCreditMinRateMinusOne() {
        BigDecimal bd = Config.CREDIT_RATE_MIN_VALUE.subtract(BigDecimal.ONE);
        assertFalse(validator.validateCreditRate(bd));
    }

    @Test
    void validateCreditRateWithCreditMinRate() {
        BigDecimal bd = Config.CREDIT_RATE_MIN_VALUE;
        assertTrue(validator.validateCreditRate(bd));
    }

    @Test
    void validateCreditRateWithCreditMaxRate() {
        BigDecimal bd = Config.CREDIT_RATE_MAX_VALUE;
        assertTrue(validator.validateCreditRate(bd));
    }

    @Test
    void validateCreditRateWithCreditMaxRatePlusOne() {
        BigDecimal bd = Config.CREDIT_RATE_MAX_VALUE.add(BigDecimal.ONE);
        assertFalse(validator.validateCreditRate(bd));
    }
}