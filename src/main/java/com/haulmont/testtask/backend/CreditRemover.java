package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.Removable;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.haulmont.testtask.Setting.NO_VALID_CREDIT;

@Component
@AllArgsConstructor
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreditRemover implements Remover {
    private final CreditRepository creditRepository;

    /**
     * trying to make client {@link Credit#isUnused()}
     */
    public Result<Boolean> remove(Credit credit) {
        try {
            if (credit.getCreditId() == null || creditRepository.findById(credit.getCreditId()).isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(NO_VALID_CREDIT);

            credit.unused();
            creditRepository.save(credit);
            return Result.success(true);
        } catch (Exception exception) {
            return Result.failure(exception);
        }
    }

    @Override
    public Result<Boolean> remove(Removable removable) {
        return remove((Credit) removable);
    }
}
