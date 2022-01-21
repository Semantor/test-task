package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.backend.util.ConstraintViolationHandler;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static com.haulmont.testtask.settings.ErrorMessages.BANK_WITH_TARGET_ID_ALREADY_IN_USE;
import static com.haulmont.testtask.settings.ErrorMessages.SOME_DB_PROBLEM;

@AllArgsConstructor
@Component
public class BankSaver {
    private final BankRepository bankRepository;
    private final Validator validator;

    /**
     * trying to save bank in db
     * validate it
     * check for null Bank, bank_id.
     */
    public Result<Boolean> save(Bank bank) {
        Set<ConstraintViolation<Bank>> constraintViolations = validator.validate(bank);
        if (!constraintViolations.isEmpty())
            return Result.failure(
                    new IllegalArgumentExceptionWithoutStackTrace(
                            ConstraintViolationHandler.handleToString(constraintViolations)));
        try {
            if (bankRepository.findById(bank.getBankId()).isPresent())
                throw new IllegalArgumentExceptionWithoutStackTrace(BANK_WITH_TARGET_ID_ALREADY_IN_USE);

            bankRepository.save(bank);
        } catch (Exception exception) {
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(SOME_DB_PROBLEM));
        }
        return Result.success(true);
    }
}
