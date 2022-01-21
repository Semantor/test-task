package com.haulmont.testtask.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldNameSettings {

    //client
    public static final String CLIENT_LAST_NAME_FIELD_NAME = "lastName";
    public static final String CLIENT_FIRST_NAME_FIELD_NAME = "firstName";
    public static final String CLIENT_PATRONYMIC_FIELD_NAME = "patronymic";
    public static final String CLIENT_PHONE_NUMBER_FIELD_NAME = "phoneNumber";
    public static final String CLIENT_EMAIL_FIELD_NAME = "email";
    public static final String CLIENT_PASSPORT_FIELD_NAME = "passport";
    //bank
    public static final String BANK_NAME_FIELD_NAME = "name";
    //credit
    public static final String CREDIT_LIMIT_FIELD_NAME = "creditLimit";
    public static final String CREDIT_RATE_FIELD_NAME = "creditRate";
    //credit offer
    public static final String CREDIT_OFFER_AMOUNT_FIELD_NAME = "creditAmount";
}
