package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.Removable;
import org.jetbrains.annotations.Nullable;

public interface CreditRemover extends Remover {
    /**
     * trying to make client {@link Credit#isUnused()}
     *
     * @param credit that is being removed
     * @return true if successful removed
     * @throws com.haulmont.testtask.backend.excs.CreditDeleteException due to failed, fe credit is not persist
     */
    boolean remove(@Nullable Credit credit);

    @Override
    default boolean remove(@Nullable Removable removable){
        return remove((Credit) removable);
    }
}
