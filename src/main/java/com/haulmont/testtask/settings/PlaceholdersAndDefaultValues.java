package com.haulmont.testtask.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaceholdersAndDefaultValues {
    //placeholders
    public static final String FIRST_NAME_PLACEHOLDER = "Ivan";
    public static final String LAST_NAME_PLACEHOLDER = "Ivanov";
    public static final String PATRONYMIC_PLACEHOLDER = "Ivanovich";
    public static final String PHONE_NUMBER_PLACEHOLDER = "(9**)***-**-**";
    public static final String EMAIL_PLACEHOLDER = "example@email.com";
    public static final String PASSPORT_PLACEHOLDER = "passport";
    public static final String PASSPORT_SERIES_PLACEHOLDER = "0000";
    public static final String PASSPORT_NUMBER_PLACEHOLDER = "000000";
    public static final String SEARCH_FIELD_PLACEHOLDER = "search";

    //text default values
    public static final String NAME_FIELD_DEFAULT_VALUE = "";
    public static final String PHONE_FIELD_DEFAULT_VALUE = "";
    public static final String EMAIL_FIELD_DEFAULT_VALUE = "";
    public static final String PASSPORT_FIELD_DEFAULT_VALUE = "";
    public static final String CREDIT_LIMIT_DEFAULT_VALUE = "$ 0.00";
    public static final String CREDIT_RATE_DEFAULT_VALUE = "% 0.00";
}
