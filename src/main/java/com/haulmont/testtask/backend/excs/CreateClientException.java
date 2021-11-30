package com.haulmont.testtask.backend.excs;

public class CreateClientException extends IllegalStateException {
    public static final String EMPTY_UUID = "empty client id";
    public static final String WRONG_UUID = "Wrong uuid";
    public static final String UUID_IS_ALREADY_USED = "this id is already used";


    public static final String EMPTY_LASTNAME = "lastname field is empty";
    public static final String TOO_SHORT_LASTNAME = "lastname min length is 3";
    public static final String LASTNAME_INCORRECT_SYMBOLS = "incorrect symbols in lastname";

    public static final String EMPTY_NAME = "firstname field is empty";
    public static final String TOO_SHORT_NAME = "name min length is 3";
    public static final String NAME_INCORRECT_SYMBOLS = "incorrect symbols in firstname";

    public static final String NO_VALID_EMAIL = "email is not valid";
    public static final String NO_VALID_PHONE = "phone is not valid";
    public static final String NO_VALID_PASSPORT = "passport is not valid";
    public static final String DB_PROBLEM = "some database problem occurred";
    public static final String DOES_NOT_PRESENT = "this client does not present in database";

    public CreateClientException(String s) {
        super(s);
    }
}
