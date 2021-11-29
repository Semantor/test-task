package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Removable;
import org.jetbrains.annotations.Nullable;

public interface BankRemover extends Remover {

    /**
     * trying to delete bank
     * on delete cascade credits, then credit offers, and then payments
     *
     * @param bank that is being removed
     * @return true if successful removed
     * @throws com.haulmont.testtask.backend.excs.BankDeleteException due to failed, fe bank isnt persist
     */
    boolean remove(@Nullable Bank bank);

    @Override
    default boolean remove(@Nullable Removable removable){
        return remove((Bank) removable);
    }
}
