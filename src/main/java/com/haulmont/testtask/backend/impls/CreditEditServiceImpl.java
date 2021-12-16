package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditEditService;
import com.haulmont.testtask.backend.excs.CreditDeleteException;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
public class CreditEditServiceImpl implements CreditEditService {
    private final CreditRepository creditRepository;

    @Override
    @Transactional
    public void edit(@NotNull Credit oldPersistCredit, @NotNull Credit newNonPersist) {
        Optional<Credit> byId = creditRepository.findById(oldPersistCredit.getCreditId());
        if (byId.isEmpty()) throw new CreditDeleteException("old credit is non persist");
        if (byId.get().isUnused()) throw new CreditDeleteException("already unused");

        oldPersistCredit.unused();
        creditRepository.save(oldPersistCredit);
        creditRepository.save(newNonPersist);
    }
}
