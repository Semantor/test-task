package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditConstraintProvider;
import com.haulmont.testtask.backend.CreditSaver;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.BankRepository;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.haulmont.testtask.Setting.*;
import static com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace.amountErrorMsg;
import static com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace.rateErrorMsg;

@Component
@AllArgsConstructor
public class CreditSaverImpl implements CreditSaver {

    private final CreditRepository creditRepository;
    private final BankRepository bankRepository;
    private final Validator validator;
    private final CreditConstraintProvider creditConstraintProvider;

    @Override
    public void save(@Nullable Credit credit) throws IllegalArgumentExceptionWithoutStackTrace {
        if (credit == null)
            throw new IllegalArgumentExceptionWithoutStackTrace(NULLABLE_CREDIT);
        if (credit.getCreditId() == null)
            throw new IllegalArgumentExceptionWithoutStackTrace(NULLABLE_ID);
        if (!validator.validateCreditAmount(credit.getCreditLimit()))
            throw new IllegalArgumentExceptionWithoutStackTrace(amountErrorMsg(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE,creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE));
        if (!validator.validateCreditRate(credit.getCreditRate()))
            throw new IllegalArgumentExceptionWithoutStackTrace(rateErrorMsg(creditConstraintProvider.CREDIT_RATE_MIN_VALUE, creditConstraintProvider.CREDIT_RATE_MAX_VALUE));
        if (credit.getBank() == null)
            throw new IllegalArgumentExceptionWithoutStackTrace(NULLABLE_BANK_FIELD);
        if (credit.getBank().getBankId() == null)
            throw new IllegalArgumentExceptionWithoutStackTrace(NULLABLE_BANK_ID_IN_CREDIT);

        Optional<Bank> bank = bankRepository.findById(credit.getBank().getBankId());
        if (bank.isEmpty())
            return;
        credit.setBank(bank.get());
        creditRepository.save(credit);
    }
}
