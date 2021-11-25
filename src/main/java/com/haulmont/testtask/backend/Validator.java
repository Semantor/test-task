package com.haulmont.testtask.backend;

import com.haulmont.testtask.Config;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
public class Validator {
    /**
     * check for null, empty and correct
     */
    public @Nullable UUID validateUUID(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            log.warn("incoming empty UUID :" + uuid);
            return null;
        }
        UUID fromString;
        try {
            fromString = UUID.fromString(uuid);
        } catch (IllegalArgumentException ex) {
            log.warn("wrong UUID: " + uuid);
            return null;
        }
        return fromString;
    }

    public final boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }


    public boolean validateCreditAmount(BigDecimal verifiableCreditAmount) {
        return validateCreditAmount(verifiableCreditAmount, Config.CREDIT_LIMIT_MAX_VALUE);
    }

    public boolean validateCreditAmount(BigDecimal verifiableCreditAmount, BigDecimal creditLimit) {
        return verifiableCreditAmount.compareTo(Config.CREDIT_LIMIT_MIN_VALUE) > -1
                && verifiableCreditAmount.compareTo(creditLimit) < 1;
    }

    public boolean validateCreditRate(BigDecimal verifiableCreditRate) {
        return verifiableCreditRate.compareTo(Config.CREDIT_RATE_MAX_VALUE) < 1 &&
                verifiableCreditRate.compareTo(Config.CREDIT_RATE_MIN_VALUE) > -1;
    }

}
