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
        try {
            return clientRepository.findAll(clientSpecifications.getByPhoneExactMatch(phone)).isEmpty();
        } catch (Exception exception) {
            return true;
        }
    }

    public boolean isAvailableEmail(String email) {
        try {
            return clientRepository.findAll(clientSpecifications.getByEmailExactMatch(email)).isEmpty();
        } catch (Exception exception) {
            return true;
        }
    }

    public boolean isAvailablePassport(String passport) {
        try {
            return clientRepository.findAll(clientSpecifications.getByPassportExactMatch(passport)).isEmpty();
        } catch (Exception exception) {
            return true;
        }
    }
}
