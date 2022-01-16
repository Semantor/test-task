package com.haulmont.testtask.backend.util;

import java.math.BigDecimal;

import static com.haulmont.testtask.Setting.*;

public class IllegalArgumentExceptionWithoutStackTrace extends RuntimeException {

    public static String amountErrorMsg(BigDecimal creditLimitMinValue, BigDecimal creditLimitMaxValue) {
        return WRONG_AMOUNT_MUST_BE_MORE_OR_EQUAL +
                creditLimitMinValue +
                AND_LESS_THEN +
                creditLimitMaxValue;
    }

    public static String rateErrorMsg(BigDecimal creditRateMinValue, BigDecimal creditRateMaxValue) {
        return WRONG_RATE_MUST_BE_MORE_OR_EQUAL +
                creditRateMinValue +
                AND_LESS_THEN +
                creditRateMaxValue;
    }

    public IllegalArgumentExceptionWithoutStackTrace(String message) {
        super(message, null, false, false);
    }
}
