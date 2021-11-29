package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.BankProvider;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
public class BankProviderImpl implements BankProvider {

    private final BankRepository bankRepository;
    private final Validator validator;

    @Override
    public Optional<Bank> getBank(String uuid) {
        UUID uuid1 = validator.validateUUID(uuid);
        if (uuid1 == null) return Optional.empty();
        return bankRepository.findById(uuid1);
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    @Override
    public void updateClients(Bank bank) {
        if (bank == null || bank.getBankId() == null || bankRepository.findById(bank.getBankId()).isEmpty()) return;
        bank.setClients(bankRepository.getBankClients(bank));
    }
}