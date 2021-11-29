package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditProvider;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class CreditProviderNonUnusedImpl implements CreditProvider {
    private final CreditRepository creditRepository;
    private final Validator validator;

    @Override
    public List<Credit> getAllCredit() {
        return creditRepository.findByIsUnused(false);
    }

    @Override
    public Optional<Credit> getCredit(@Nullable String uuid) {
        UUID uuid1 = validator.validateUUID(uuid);
        if (uuid1 == null) return Optional.empty();
        Optional<Credit> byId = creditRepository.findById(uuid1);
        if (byId.isEmpty() || byId.get().isUnused()) return Optional.empty();
        return byId;
    }
}
