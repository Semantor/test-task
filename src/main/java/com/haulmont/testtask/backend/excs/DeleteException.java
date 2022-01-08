package com.haulmont.testtask.backend.excs;

public class DeleteException extends RuntimeException {

    public DeleteException(String message) {
        super(message);
    }
}
