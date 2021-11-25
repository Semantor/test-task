package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Bank;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface BankProvider {
    /**
     * Provide bank buy its id from database.
     * validating incoming param
     */
    Optional<Bank> getBank(@Nullable String UUID);

    /**
     * @return all banks list or {@link Collections#emptyList()} due to some problem
     */
    List<Bank> getAllBanks();

    /**
     * add or update to target bank all its clients in {@link Bank#getClients()} field
     */
    void updateClients(Bank bank);
}
