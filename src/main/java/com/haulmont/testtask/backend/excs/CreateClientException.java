package com.haulmont.testtask.backend.excs;

public class CreateClientException extends IllegalStateException {
    public CreateClientException(String s) {
        super(s);
    }
}
