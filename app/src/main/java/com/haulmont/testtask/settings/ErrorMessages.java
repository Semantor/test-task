package com.haulmont.testtask.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {
    public static final String NO_ERRORS = "No errors";
    public static final String ENUMERATION_ERRORS = "errors: ";
    public static final String ERROR_TEXT_DELIMITER = ". ";
    public static final String MUST_BE_MORE_THAN_ZERO = "must be more then zero";
    public static final String WRONG_INCOME_DATA = "wrong incoming data";
    public static final String MUST_BE_LETTER_ERROR = "You can use only letter symbols";
    public static final String SOME_DB_PROBLEM = "some db problem";
    public static final String UNIVERSAL_NAME_ERROR = "This field is required. Must be minimum three letter without other symbols";

    //uuid
    public static final String NULLABLE_ID = "id does not present";
    public static final String UUID_IS_ALREADY_USED = "this id is already used";
    //client field
    public static final String EMPTY_LASTNAME = "lastname field is empty";
    public static final String EMPTY_NAME = "name field is empty";
    public static final String MUST_BE_MINIMUM_THREE_SYMBOLS_IN_FIRST_NAME = "First name must be minimum three symbols";
    public static final String LASTNAME_INCORRECT_SYMBOLS = "incorrect symbols in lastname";
    public static final String NAME_INCORRECT_SYMBOLS = "incorrect symbols in firstname";
    public static final String MUST_BE_MINIMUM_THREE_SYMBOLS_IN_LAST_NAME = "Last name ust be minimum three symbols";
    public static final String PATRONYMIC_ERROR = "This field is not required, cannot be length one or two. Must be use letter only";
    public static final String INCORRECT_EMAIL_ADDRESS_ERROR_MSG = "Incorrect email address";
    public static final String NO_VALID_PHONE_ERROR_MSG = "not valid phone number, must be 10 digits starting with 9";
    public static final String EMPTY_PHONE_NUMBER = "phone is required";
    public static final String PHONE_ALREADY_IN_USE_ERROR_MSG = "this phone is already in use";
    public static final String NO_VALID_EMAIL = "email is not valid";
    public static final String EMPTY_EMAIL = "email is required";
    public static final String EMAIL_ALREADY_IN_USE_ERROR_MSG = "this email is already in use";
    public static final String NO_VALID_PASSPORT = "passport is not valid";
    public static final String EMPTY_PASSPORT = "passport is required";
    public static final String PASSPORT_LENGTH_ERROR_MSG =
            "Passport series is exactly 4 digits length, Numbers 6. Third + forth in series is year of passport receive";
    public static final String PASSPORT_ALREADY_IN_USE_ERROR_MSG = "this passport already in use";
    public static final String PHONE_LENGTH_ERROR_MSG = "phone must be exactly 10 digits";
    public static final String PHONE_PATTERN_ERROR_MSG = "phone must be start with 9";

    //client
    public static final String NULLABLE_CLIENT = "nullable client";
    public static final String CLIENT_DOES_NOT_PRESENT_IN_DB = "this client does not present in db";
    public static final String NO_VALID_CLIENT = "not a valid client";
    public static final String HAVE_ACTIVE_CREDIT_OFFER = "Have active Credit offer";
    //credit
    public static final String NULLABLE_CREDIT = "nullable credit";
    public static final String NO_VALID_CREDIT = "no valid credit";
    public static final String OLD_CREDIT_IS_NON_PERSIST = "old credit is non persist";
    public static final String ALREADY_UNUSED = "already unused";
    public static final String MUST_CHOOSE_BANK_ERROR_MSG = "Bank is required";
    public static final String WRONG_CREDIT_LIMIT_MESSAGE = "wrong credit limit value";
    public static final String WRONG_CREDIT_RATE_MESSAGE = "wrong credit rate value";
    //bank
    public static final String BANK_DOES_NOT_EXIST = "bank does not exists";
    public static final String NULLABLE_BANK = "bank is nullable";
    public static final String BANK_WITH_TARGET_ID_ALREADY_IN_USE = "bank id is already persist";
    public static final String NAME_IS_ALREADY_EXISTS = "This name is already exists ";
    public static final String MUST_BE_MINIMUM_THREE_SYMBOLS_IN_BANK_NAME = "Bank name must be minimum three symbols";


    //credit offer
    public static final String NULLABLE_CREDIT_OFFER = "Empty credit offer";
    public static final String NOT_VALID_MONTH_COUNT = "not valid month count";
    public static final String PAYMENTS_IS_NOT_CONNECTED_TO_CREDIT_OFFER = "payments is not connected to credit offer";
    public static final String NOT_VALID_CREDIT_AMOUNT = "not valid credit amount";
    public static final String WRONG_PAYMENTS_COUNT = "wrong payments count";
    public static final String CREDIT_OFFER_DOES_NOT_EXIST = "credit offer does not exist";
    public static final String PAYMENT_PERIOD_IS_ALREADY_START_AND_DOES_NOT_END = "payment period is already start and doesn't end";
    public static final String TOO_MANY_MONTH_COUNT = "too long period in months, choose a smaller one";
    public static final String WRONG_MONTH_COUNT_INPUT = "wrong month count";
    public static final String WRONG_DATE_IS_PICKED = "wrong date is picked";
    public static final String START_DATE_MUST_BE_EARLY_THAN_END_DATE = "start date must be early than end date";

    //payment
    public static final String WRONG_INCOME_MONTH = "wrong income data - month in Payment Calculator";
    public static final String WRONG_INCOME_AMOUNT = "wrong income data - amount in Payment Calculator";
    public static final String WRONG_INCOME_RECEIVING_DATE = "wrong income data - date of receiving in Payment Calculator";
    public static final String CREDIT_RATE_MUST_BE_MORE_OR_EQUAL_ZERO = "credit rate must be more or equal ZERO";
    public static final String CREDIT_REMAIN_BALANCE_MUST_BE_MORE_OR_EQUAL_ZERO = "credit remain balance must be more or equal ZERO";

    public static final String WRONG_AMOUNT_MUST_BE_MORE_OR_EQUAL =
            "Wrong credit amount: must be more or equal ";
    public static final String WRONG_RATE_MUST_BE_MORE_OR_EQUAL =
            "Wrong credit rate: must be more or equal ";
    public static final String AND_LESS_THEN = " and less then ";
}
