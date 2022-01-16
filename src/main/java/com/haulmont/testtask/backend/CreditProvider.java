package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.excs.Result;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class CreditProvider {
    private final CreditRepository creditRepository;

    public Result<List<Credit>> getAllCredit() {
        try {
            return Result.success(creditRepository.findByIsUnused(false));
        } catch (Exception ex) {
            return Result.failure(ex);
        }
    }
}
