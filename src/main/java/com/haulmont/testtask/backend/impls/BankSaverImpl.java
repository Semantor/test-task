package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.BankSaver;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BankSaverImpl implements BankSaver {
    private final BankRepository bankRepository;

    @Override
    public void save(Bank bank) {
        if (bank == null) return;
        bankRepository.save(bank);
    }
}
