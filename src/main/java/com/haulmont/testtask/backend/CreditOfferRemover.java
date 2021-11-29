package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Removable;
import org.jetbrains.annotations.Nullable;

public interface CreditOfferRemover extends Remover {
    /**
     * trying to make client {@link CreditOffer#isCanceled()}
     *
     * @param creditOffer that is being removed
     * @return true if successful removed
     * @throws com.haulmont.testtask.backend.excs.ClientDeleteException due to failed, fe it was created over a month ago
     */
    boolean remove(@Nullable CreditOffer creditOffer);

    @Override
    default boolean remove(@Nullable Removable removable){
        return remove((CreditOffer) removable);
    }
}
