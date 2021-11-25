package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Bank;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface BankProvider {
    /**
     * search bank by its id
     *
     * @param UUID bank id
     */
    Optional<Bank> getBank(String UUID);

    /**
     * prepare list of all banks
     *
     * @return all banks list or {@link Collections#emptyList()}
     */
    List<Bank> getAllBanks();

    /**
     * add to target bank all its clients in {@link Bank#getClients()} field
     */
    void updateClients(Bank bank);
}
