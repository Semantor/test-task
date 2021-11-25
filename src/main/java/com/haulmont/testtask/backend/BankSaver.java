package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Bank;
import org.jetbrains.annotations.Nullable;

public interface BankSaver {
    /**
     * trying to save bank in db
     * validate it
     * check for null Bank, bank_id.
     *
     * @throws com.haulmont.testtask.backend.excs.CreateBankException due to fail
     */
    void save(@Nullable Bank bank);
}
