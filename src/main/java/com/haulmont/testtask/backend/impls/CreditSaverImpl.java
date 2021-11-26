package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditSaver;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.BankRepository;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Optional;

@AllArgsConstructor
public class CreditSaverImpl implements CreditSaver {

    private final CreditRepository creditRepository;
    private final BankRepository bankRepository;
    private final Validator validator;

    @Override
    public void save(@Nullable Credit credit) {
        if (credit==null) return;
        if (credit.getCreditId()==null) return;
        if (!validator.validateCreditAmount(credit.getCreditLimit())) return;
        if (!validator.validateCreditRate(credit.getCreditRate())) return;
        Optional<Bank> bank;
        if (credit.getBank()==null || credit.getBank().getBankId()==null ||
                (bank = bankRepository.findById(credit.getBank().getBankId())).isEmpty())
            return;
        credit.setBank(bank.get());

        creditRepository.save(credit);
    }
}
