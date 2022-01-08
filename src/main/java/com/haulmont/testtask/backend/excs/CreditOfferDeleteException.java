package com.haulmont.testtask.backend.excs;

public class CreditOfferDeleteException extends DeleteException {
    public static final String CREDIT_OFFER_DOES_NOT_EXIST = "credit offer does not exist";
    public static final String PAYMENT_PERIOD_IS_ALREADY_START_AND_DOESNT_END = "payment period is already start and doesn't end";

    public CreditOfferDeleteException(String message) {
        super(message);
    }

}
