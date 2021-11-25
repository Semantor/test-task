package com.haulmont.testtask.backend.excs;

public class CreateBankException extends RuntimeException {
    public CreateBankException(String message) {
        super(message);
    }
}
