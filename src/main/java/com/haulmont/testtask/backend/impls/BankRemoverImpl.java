/*
 * Copyright (c) 11/26/21, 5:42 PM.
 * created by fred
 */

package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.BankRemover;
import com.haulmont.testtask.backend.excs.BankDeleteException;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class BankRemoverImpl implements BankRemover {
    private final BankRepository bankRepository;

    @Override
    public boolean remove(@Nullable Bank bank) {
        if (bank == null || bank.getBankId() == null || bankRepository.findById(bank.getBankId()).isEmpty())
            throw new BankDeleteException(BankDeleteException.BANK_DOES_NOT_EXIST);

        bankRepository.delete(bank);

        return true;
    }
}
