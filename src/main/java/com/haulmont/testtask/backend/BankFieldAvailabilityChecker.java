package com.haulmont.testtask.backend;

import org.jetbrains.annotations.NotNull;

/**
 * check for availability field value
 * return false if this value already in used or incorrect
 */
public interface BankFieldAvailabilityChecker {
    boolean isAvailableName(@NotNull String bankName);

}
