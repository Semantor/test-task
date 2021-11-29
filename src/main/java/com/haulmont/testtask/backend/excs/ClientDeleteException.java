package com.haulmont.testtask.backend.excs;

public class ClientDeleteException extends DeleteException{

    public static final String  HAVE_ACTIVE_CREDIT_OFFER = "Have active Credit offer";
    public static final String  NON_VALID = "not a valid user";
    public ClientDeleteException() {
    }

    public ClientDeleteException(String message) {
        super(message);
    }

    public ClientDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientDeleteException(Throwable cause) {
        super(cause);
    }

    public ClientDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
