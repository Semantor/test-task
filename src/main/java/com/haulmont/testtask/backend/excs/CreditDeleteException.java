package com.haulmont.testtask.backend.excs;

public class CreditDeleteException extends DeleteException{
    public CreditDeleteException() {
    }

    public CreditDeleteException(String message) {
        super(message);
    }

    public CreditDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditDeleteException(Throwable cause) {
        super(cause);
    }

    public CreditDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
