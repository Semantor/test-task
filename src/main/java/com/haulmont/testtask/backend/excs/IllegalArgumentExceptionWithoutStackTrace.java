package com.haulmont.testtask.backend.excs;

import com.haulmont.testtask.Config;

import static com.haulmont.testtask.Setting.*;

public class IllegalArgumentExceptionWithoutStackTrace extends RuntimeException {

    public static String amountErrorMsg() {
        return WRONG_AMOUNT_MUST_BE_MORE_OR_EQUAL +
                Config.CREDIT_LIMIT_MIN_VALUE +
                AND_LESS_THEN +
                Config.CREDIT_LIMIT_MAX_VALUE;
    }

    public static String rateErrorMsg() {
        return WRONG_RATE_MUST_BE_MORE_OR_EQUAL +
                Config.CREDIT_RATE_MIN_VALUE +
                AND_LESS_THEN +
                Config.CREDIT_RATE_MAX_VALUE;
    }

    public IllegalArgumentExceptionWithoutStackTrace(String message) {
        super(message, null, false, false);
    }
}
