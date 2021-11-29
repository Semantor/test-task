package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditRemover;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.Removable;

public class DummyCreditRemoverImpl implements CreditRemover {
    @Override
    public boolean remove(Credit credit) {
        return false;
    }

    @Override
    public boolean remove(Removable removable) {
        return remove((Credit) removable);
    }
}
