package com.haulmont.testtask.backend;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.excs.Result;
import com.haulmont.testtask.backend.util.ProblemKeeper;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

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
                            ProblemKeeper.of(constraintViolations).toString()));
        try {
            bankRepository.save(bank);
        } catch (Exception exception) {
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(Setting.SOME_DB_PROBLEM));
        }
        return Result.success(true);
    }
}
