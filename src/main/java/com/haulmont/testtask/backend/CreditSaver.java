package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Credit;
import org.jetbrains.annotations.Nullable;

public interface CreditSaver {
    /**
     * validate credit and its own uuid.
     * check if this uuid already in use.
     * check bank is present.
     * use credit's limits:
     * {@link com.haulmont.testtask.Config#CREDIT_LIMIT_MIN_VALUE}
     * {@link com.haulmont.testtask.Config#CREDIT_LIMIT_MAX_VALUE}
     * {@link com.haulmont.testtask.Config#CREDIT_RATE_MIN_VALUE}
     * {@link com.haulmont.testtask.Config#CREDIT_RATE_MAX_VALUE}
     */
    void save(@Nullable Credit credit);
}
