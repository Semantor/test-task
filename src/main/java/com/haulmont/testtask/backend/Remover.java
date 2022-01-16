package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.excs.Result;
import com.haulmont.testtask.model.entity.Removable;

import javax.validation.constraints.NotNull;

public interface Remover {
    /**
     * trying to remove.
     * validate first
     */
    Result<Boolean> remove(@NotNull Removable removable);
}
