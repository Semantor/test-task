package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Credit;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface CreditProvider {

    List<Credit> getAllCredit();

    /**
     * validate UUID
     */
    Optional<Credit> getCredit(@Nullable String uuid);
}
