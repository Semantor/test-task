package com.haulmont.testtask.backend;

public interface ClientFieldAvailabilityChecker {
    boolean isAvailablePhone(String phone);

    boolean isAvailableEmail(String email);

    boolean isAvailablePassport(String passport);
}
