package com.haulmont.testtask.backend.excs;

public class CreateCreditOfferException extends RuntimeException {

    public static final String NOT_VALID_MONTH_COUNT = "not valid month count";

    public CreateCreditOfferException(String message) {
        super(message);
    }

}
