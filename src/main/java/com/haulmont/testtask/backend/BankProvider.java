package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Bank;

import java.util.List;
import java.util.Optional;

public interface BankProvider {
    Optional<Bank> getBank(String UUID);
    List<Bank> getAllBanks();
    void updateClients(Bank bank);
}
