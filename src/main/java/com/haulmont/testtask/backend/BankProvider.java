package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
@Slf4j
@Component
public class BankProvider {
    private final BankRepository bankRepository;
    private final StringUUIDHandler uuidHandler;

    /**
     * Provide bank buy its id from database.
     * validating incoming param
     */
    public Optional<Bank> getBank(String uuidString) {
        UUID bankUUID = uuidHandler.validateStringUUIDAndReturnNullOrUUID(uuidString);
        if (bankUUID == null) return Optional.empty();
        return bankRepository.findById(bankUUID);
    }

    /**
     * @return all banks list or {@link Collections#emptyList()} due to some problem
     */
    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }


    /**
     * add or update to target bank all its clients in {@link Bank#getClients()} field
     */
    public void updateClients(Bank bank) {
        if (bank == null || bank.getBankId() == null || bankRepository.findById(bank.getBankId()).isEmpty()) return;
        bank.setClients(bankRepository.getBankClients(bank));
    }
}
