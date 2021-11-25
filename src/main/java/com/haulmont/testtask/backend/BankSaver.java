package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Bank;

public interface BankSaver {
    /**
     * trying to save bank in db
     * validate it
     * @param bank to save
     * @throws com.haulmont.testtask.backend.excs.CreateBankException due to fail
     */
    void save(Bank bank);
}
