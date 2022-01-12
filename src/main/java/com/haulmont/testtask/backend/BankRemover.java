package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.excs.BankDeleteException;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Removable;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import static com.haulmont.testtask.Setting.BANK_DOES_NOT_EXIST;

@AllArgsConstructor
@Component
public class BankRemover implements Remover {
    private final BankRepository bankRepository;

    /**
     * trying to delete bank
     * on delete cascade credits, then credit offers, and then payments
     *
     * @param bank that is being removed
     * @return true if successful removed
     * @throws com.haulmont.testtask.backend.excs.BankDeleteException due to failed, fe bank isnt persist
     */
    public boolean remove(@Nullable Bank bank) {
        if (bank == null || bank.getBankId() == null || bankRepository.findById(bank.getBankId()).isEmpty())
            throw new BankDeleteException(BANK_DOES_NOT_EXIST);

        bankRepository.delete(bank);

        return true;
    }

    @Override
    public boolean remove(@Nullable Removable removable) {
        return remove((Bank) removable);
    }
}
