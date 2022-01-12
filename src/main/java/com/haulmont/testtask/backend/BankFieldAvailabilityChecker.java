package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * check for availability field value
 * return false if this value already in used or incorrect
 * using for real time computing in client form validation
 */
@Component
@AllArgsConstructor
public class BankFieldAvailabilityChecker {
    private final BankRepository bankRepository;

    public boolean isAvailableName(@NotNull String bankName) {
        return bankRepository.findByName(bankName).isEmpty();
    }
}
