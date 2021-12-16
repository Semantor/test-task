package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Credit;
import org.jetbrains.annotations.NotNull;

public interface CreditEditService {
    /**
     * trying to edit service due to business logic.
     *
     * @throws com.haulmont.testtask.backend.excs.CreditDeleteException if old credit already unused or non persist
     */
    void edit(@NotNull Credit oldPersistCredit, @NotNull Credit newNonPersist);
}
