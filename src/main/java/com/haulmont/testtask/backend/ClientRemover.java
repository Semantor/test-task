package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Client;
import org.jetbrains.annotations.Nullable;

public interface ClientRemover extends Remover {
    /**
     * trying to make client {@link Client#isRemoved()}
     *
     * @param client that is being removed
     * @return true if successful removed
     * @throws com.haulmont.testtask.backend.excs.ClientDeleteException due to failed, fe client has active credit offer
     */
    boolean remove(@Nullable Client client);
}
