package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.BankRemover;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Removable;

public class DummyBankRemoverImpl implements BankRemover {
    @Override
    public boolean remove(Bank bank) {
        return false;
    }

    @Override
    public boolean remove(Removable removable) {
        return remove((Bank) removable);
    }
}
