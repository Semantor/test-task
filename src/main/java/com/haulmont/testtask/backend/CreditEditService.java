package com.haulmont.testtask.backend;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.excs.Result;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.haulmont.testtask.Setting.ALREADY_UNUSED;
import static com.haulmont.testtask.Setting.OLD_CREDIT_IS_NON_PERSIST;

@RequiredArgsConstructor
@Component
public class CreditEditService {
    private final CreditRepository creditRepository;

    /**
     * trying to edit service due to business logic.
     */
    @Transactional
    public Result<Boolean> edit(@NotNull Credit oldPersistCredit, @NotNull Credit newNonPersist) {
        try {
            if (creditRepository.findById(newNonPersist.getCreditId()).isPresent())
                throw new IllegalArgumentExceptionWithoutStackTrace(Setting.UUID_IS_ALREADY_USED);
            Optional<Credit> optionalOldCredit = creditRepository.findById(oldPersistCredit.getCreditId());
            if (optionalOldCredit.isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(OLD_CREDIT_IS_NON_PERSIST);
            if (optionalOldCredit.get().isUnused())
                throw new IllegalArgumentExceptionWithoutStackTrace(ALREADY_UNUSED);

            oldPersistCredit.unused();
            creditRepository.save(oldPersistCredit);
            creditRepository.save(newNonPersist);
        } catch (Exception ex) {
            return Result.failure(ex);
        }
        return Result.success(true);
    }
}
