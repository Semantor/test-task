/*
 * Copyright (c) 11/26/21, 2:35 PM.
 * created by fred
 */

package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditRemover;
import com.haulmont.testtask.backend.excs.CreditDeleteException;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class CreditRemoverImpl implements CreditRemover {
    private final CreditRepository creditRepository;
    @Override
    public boolean remove(@Nullable Credit credit) {
        if (credit==null||credit.getCreditId() == null||creditRepository.findById(credit.getCreditId()).isEmpty())
            throw new CreditDeleteException(CreditDeleteException.NO_VALID_CREDIT);

        credit.unused();
        creditRepository.save(credit);
        return true;
    }
}
