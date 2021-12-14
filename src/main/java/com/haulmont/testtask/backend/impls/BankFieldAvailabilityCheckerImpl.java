package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.BankFieldAvailabilityChecker;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class BankFieldAvailabilityCheckerImpl implements BankFieldAvailabilityChecker {
    private final BankRepository bankRepository;

    @Override
    public boolean isAvailableName(@NotNull String bankName) {
        return bankRepository.findByName(bankName).isEmpty();
    }
}
