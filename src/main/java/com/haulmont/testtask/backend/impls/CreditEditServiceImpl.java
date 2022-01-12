package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditEditService;
import com.haulmont.testtask.backend.excs.CreditDeleteException;
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
public class CreditEditServiceImpl implements CreditEditService {
    private final CreditRepository creditRepository;

    @Override
    @Transactional
    public void edit(@NotNull Credit oldPersistCredit, @NotNull Credit newNonPersist) {
        Optional<Credit> byId = creditRepository.findById(oldPersistCredit.getCreditId());
        if (byId.isEmpty()) throw new CreditDeleteException(OLD_CREDIT_IS_NON_PERSIST);
        if (byId.get().isUnused()) throw new CreditDeleteException(ALREADY_UNUSED);

        oldPersistCredit.unused();
        creditRepository.save(oldPersistCredit);
        creditRepository.save(newNonPersist);
    }
}
