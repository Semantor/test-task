package com.haulmont.testtask.backend.excs;

public class CreditDeleteException extends DeleteException {

    public static final String OLD_CREDIT_IS_NON_PERSIST = "old credit is non persist";
    public static final String ALREADY_UNUSED = "already unused";
    public static final String NO_VALID_CREDIT = "no valid credit";

    public CreditDeleteException(String message) {
        super(message);
    }
}
