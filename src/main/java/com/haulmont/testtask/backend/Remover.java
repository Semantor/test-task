package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Removable;
import org.jetbrains.annotations.Nullable;

public interface Remover {
    /**
     * trying to remove.
     * validate first
     *
     * @throws com.haulmont.testtask.backend.excs.DeleteException due to fail
     */
    boolean remove(@Nullable Removable removable);
}
