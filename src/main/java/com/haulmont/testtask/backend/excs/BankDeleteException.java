package com.haulmont.testtask.backend.excs;

public class BankDeleteException extends DeleteException{
    public BankDeleteException() {
    }

    public BankDeleteException(String message) {
        super(message);
    }

    public BankDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public BankDeleteException(Throwable cause) {
        super(cause);
    }

    public BankDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
