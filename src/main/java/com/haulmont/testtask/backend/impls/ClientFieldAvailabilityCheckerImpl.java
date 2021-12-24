package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientFieldAvailabilityChecker;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.model.repositories.ClientSpecifications;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientFieldAvailabilityCheckerImpl implements ClientFieldAvailabilityChecker {
    private final ClientRepository clientRepository;
    private final ClientSpecifications clientSpecifications;

    @Override
    public boolean isAvailablePhone(String phone) {
        return clientRepository.findAll(clientSpecifications.getByPhoneExactMatch(phone)).isEmpty();
    }

    @Override
    public boolean isAvailableEmail(String email) {
        return clientRepository.findAll(clientSpecifications.getByEmailExactMatch(email)).isEmpty();
    }

    @Override
    public boolean isAvailablePassport(String passport) {
        return clientRepository.findAll(clientSpecifications.getByPassportExactMatch(passport)).isEmpty();
    }
}
