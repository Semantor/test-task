package com.haulmont.testtask.backend;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.repositories.BankRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@AllArgsConstructor
@Slf4j
@Component
public class BankProvider {
    private final BankRepository bankRepository;


    /**
     * @return all banks list or {@link Collections#emptyList()} due to some problem
     */
    public List<Bank> getAllBanks() {
        try {
            return bankRepository.findAll();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }


    /**
     * add or update to target bank all its clients in {@link Bank#getClients()} field
     */
    public Result<Boolean> updateClients(Bank bank) {
        try {
            if (bank.getBankId() == null || bankRepository.findById(bank.getBankId()).isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(Setting.WRONG_INCOME_DATA);
            bank.setClients(bankRepository.getBankClients(bank));
        } catch (Exception ex) {
            return Result.failure(ex);
        }
        return Result.success(true);
    }
}
