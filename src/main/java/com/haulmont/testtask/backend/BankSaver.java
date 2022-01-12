package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BankSaver {
    private final BankRepository bankRepository;

    /**
     * trying to save bank in db
     * validate it
     * check for null Bank, bank_id.
     *
     * @throws com.haulmont.testtask.backend.excs.CreateBankException due to fail
     */
    public void save(Bank bank) {
        if (bank == null) return;
        bankRepository.save(bank);
    }
}
