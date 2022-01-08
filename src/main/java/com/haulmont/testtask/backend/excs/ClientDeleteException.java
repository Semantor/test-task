package com.haulmont.testtask.backend.excs;

public class ClientDeleteException extends DeleteException{

    public static final String  HAVE_ACTIVE_CREDIT_OFFER = "Have active Credit offer";
    public static final String NON_VALID = "not a valid user";

    public ClientDeleteException(String message) {
        super(message);
    }

}
