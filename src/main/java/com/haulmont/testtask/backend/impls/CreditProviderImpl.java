package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditProvider;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class CreditProviderImpl implements CreditProvider {

    private final CreditRepository creditRepository;
    private final Validator validator;

    @Override
    public List<Credit> getAllCredit() {
        return creditRepository.findAll();
    }


    @Override
    public Optional<Credit> getCredit(String uuidString) {
        UUID id = validator.validateStringUUIDAndReturnNullOrUUID(uuidString);
        if (id == null) return Optional.empty();
        return creditRepository.findById(id);
    }
}
