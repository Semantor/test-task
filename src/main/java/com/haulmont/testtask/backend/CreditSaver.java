package com.haulmont.testtask.backend;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.excs.Result;
import com.haulmont.testtask.backend.util.ConstraintViolationHandler;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.BankRepository;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import java.util.Optional;
import java.util.Set;

import static com.haulmont.testtask.Setting.BANK_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class CreditSaver {

    private final CreditRepository creditRepository;
    private final BankRepository bankRepository;
    private final javax.validation.Validator validator;
    private final CreditConstraintProvider creditConstraintProvider;

    /**
     * validate credit and its own uuid.
     * check if this uuid already in use.
     * check bank is present.
     * use credit's limits:
     * <p>
     * for updating use {@link CreditEditService}
     * {@link com.haulmont.testtask.backend.CreditConstraintProvider#CREDIT_LIMIT_MIN_VALUE}
     * {@link com.haulmont.testtask.backend.CreditConstraintProvider#CREDIT_LIMIT_MAX_VALUE}
     * {@link com.haulmont.testtask.backend.CreditConstraintProvider#CREDIT_RATE_MIN_VALUE}
     * {@link com.haulmont.testtask.backend.CreditConstraintProvider#CREDIT_RATE_MAX_VALUE}
     */
    @Transactional
    public Result<Boolean> save(Credit credit) throws IllegalArgumentExceptionWithoutStackTrace {
        try {
            Set<ConstraintViolation<Credit>> creditConstraintViolations = validator.validate(credit);
            if (!creditConstraintViolations.isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(ConstraintViolationHandler.handleToString(creditConstraintViolations));
            if (creditRepository.findById(credit.getCreditId()).isPresent())
                throw new IllegalArgumentExceptionWithoutStackTrace(Setting.UUID_IS_ALREADY_USED);
            Optional<Bank> optionalSelectedBank = bankRepository.findById(credit.getBank().getBankId());
            if (optionalSelectedBank.isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(BANK_DOES_NOT_EXIST);
            credit.setBank(optionalSelectedBank.get());
            creditRepository.save(credit);
        } catch (Exception ex) {
            return Result.failure(ex);
        }
        return Result.success(true);
    }
}
