package com.haulmont.testtask.backend.excs;

public class BankDeleteException extends DeleteException {
    public static final String BANK_DOES_NOT_EXIST = "bank does not exists";

    public BankDeleteException(String message) {
        super(message);
    }
}
