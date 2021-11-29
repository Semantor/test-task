package com.haulmont.testtask.backend.excs;

public class CreditOfferDeleteException extends DeleteException{
    public CreditOfferDeleteException() {
    }

    public CreditOfferDeleteException(String message) {
        super(message);
    }

    public CreditOfferDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditOfferDeleteException(Throwable cause) {
        super(cause);
    }

    public CreditOfferDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
