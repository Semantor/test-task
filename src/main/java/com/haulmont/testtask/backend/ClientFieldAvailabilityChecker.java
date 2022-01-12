package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.model.repositories.ClientSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ClientFieldAvailabilityChecker {
    private final ClientRepository clientRepository;
    private final ClientSpecifications clientSpecifications;

    public boolean isAvailablePhone(String phone) {
        return clientRepository.findAll(clientSpecifications.getByPhoneExactMatch(phone)).isEmpty();
    }

    public boolean isAvailableEmail(String email) {
        return clientRepository.findAll(clientSpecifications.getByEmailExactMatch(email)).isEmpty();
    }

    public boolean isAvailablePassport(String passport) {
        return clientRepository.findAll(clientSpecifications.getByPassportExactMatch(passport)).isEmpty();
    }
}
