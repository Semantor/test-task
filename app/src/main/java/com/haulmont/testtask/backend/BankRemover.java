package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Removable;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.haulmont.testtask.settings.ErrorMessages.BANK_DOES_NOT_EXIST;

@AllArgsConstructor
@Component
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BankRemover implements Remover {
    private final BankRepository bankRepository;

    /**
     * trying to delete bank
     * on delete cascade credits, then credit offers, and then payments
     */
    public Result<Boolean> remove(Bank bank) {
        try {
            if (bank.getBankId() == null || bankRepository.findById(bank.getBankId()).isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(BANK_DOES_NOT_EXIST);

            bankRepository.delete(bank);
        } catch (Exception ex) {
            return Result.failure(ex);
        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean> remove(Removable removable) {
        return remove((Bank) removable);
    }
}
