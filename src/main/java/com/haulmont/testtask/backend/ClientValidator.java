package com.haulmont.testtask.backend;

import org.jetbrains.annotations.Nullable;

public interface ClientValidator {
    /**
     * RFC 5322 support
     */
    boolean validateEmail(@Nullable String email);

    boolean validatePhone(@Nullable String phone);

    boolean validatePassport(@Nullable String passport);


}
