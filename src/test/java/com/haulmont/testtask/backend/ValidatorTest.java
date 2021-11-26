package com.haulmont.testtask.backend;

import com.haulmont.testtask.App;
import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfig;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@AllArgsConstructor
@ContextConfiguration(classes = {TestConfig.class, App.class, Config.class})
class ValidatorTest {

    private final Validator validator;

    @Test
    void validateUUIDWithNullInput() {
        String input = null;
        Assertions.assertNull(validator.validateUUID(input));
    }

    @Test
    void validateUUIDWithEmptyInput() {
        String input = "";
        Assertions.assertNull(validator.validateUUID(input));
    }

    @Test
    void validateUUIDWithBlankInput() {
        String input = "   ";
        Assertions.assertNull(validator.validateUUID(input));
    }

    @Test
    void validateUUIDWithRandomStringInput() {
        String input = "  asdefasd ";
        Assertions.assertNull(validator.validateUUID(input));
    }

    @Test
    void validateUUIDWithCorrectInput() {
        String input = "c9457c4d-d778-4ef4-abd8-917212cddb75";
        UUID expected = UUID.fromString("c9457c4d-d778-4ef4-abd8-917212cddb75");
        Assertions.assertEquals(expected, validator.validateUUID(input));
    }


    @Test
    void isNullOrBlank() {
        Assertions.fail();
    }

    @Test
    void validateCreditLimitWith0() {
        BigDecimal input = BigDecimal.ZERO;
        Assertions.assertFalse(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWith10() {
        BigDecimal input = BigDecimal.TEN;
        Assertions.assertFalse(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMin() {
        BigDecimal input = Config.CREDIT_LIMIT_MIN_VALUE;
        Assertions.assertTrue(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMax() {
        BigDecimal input = Config.CREDIT_LIMIT_MAX_VALUE;
        Assertions.assertTrue(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithAVG() {
        BigDecimal input = Config.CREDIT_LIMIT_MIN_VALUE.add(Config.CREDIT_LIMIT_MAX_VALUE).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
        Assertions.assertTrue(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }

    @Test
    void validateCreditLimitWithMAXadd1() {
        BigDecimal input = Config.CREDIT_LIMIT_MAX_VALUE.add(BigDecimal.ONE);
        Assertions.assertFalse(validator.validateCreditAmount(input, Config.CREDIT_LIMIT_MAX_VALUE));
    }


    @Test
    void validateCreditRate() {
        Assertions.fail();

    }
}