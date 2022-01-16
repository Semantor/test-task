package com.haulmont.testtask;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Setting {
    public static final String CREDIT_LIMIT_FORMAT = "$ %(,.2f";
    public static final String CREDIT_RATE_FORMAT = "%(,.2f";
    public static final String ONLY_LETTER_REG_EX = "[a-zA-Z]+";

    //Button settings
    public static final ButtonVariant SAVE_STYLE = ButtonVariant.LUMO_PRIMARY;
    public static final ButtonVariant CLEAR_STYLE = ButtonVariant.LUMO_ERROR;
    public static final ButtonVariant CLOSE_STYLE = ButtonVariant.MATERIAL_CONTAINED;
    public static final ButtonVariant CALCULATE_STYLE = ButtonVariant.LUMO_PRIMARY;
    public static final ButtonVariant EDIT_STYLE = ButtonVariant.LUMO_LARGE;
    public static final ButtonVariant DELETE_STYLE = ButtonVariant.LUMO_LARGE;
    public static final ButtonVariant DETAILS_STYLE = ButtonVariant.LUMO_SMALL;

    public static final String CLOSE_BUTTON_TEXT = "close";
    public static final String SAVE_BUTTON_TEXT = "save";
    public static final String CLEAR_BUTTON_TEXT = "clear";
    public static final String CALCULATE_BUTTON_TEXT = "calculate";
    public static final String EDIT_BUTTON_TEXT = "edit";
    public static final String ACCEPT_BUTTON_TEXT = "accept";
    public static final String DELETE_BUTTON_TEXT = "delete";
    public static final String DETAILS_BUTTON_TEXT = "Details";
    public static final String NEXT_BUTTON_TEXT = "next";
    public static final String UPDATE_BUTTON_TEXT = "update";
    public static final String PREVIOUS_BUTTON_TEXT = "previous";
    public static final String CREATE_NEW_BUTTON_TEXT = "create new client";
    public static final String CREATE_NEW_BANK_BUTTON_TEXT = "create new bank";
    public static final String CREATE_NEW_CREDIT_BUTTON_TEXT = "create new credit";
    public static final String SHOW_CREDIT_LIST_BUTTON_TEXT = "show credit list";
    public static final String SHOW_BANK_LIST_BUTTON_TEXT = "show bank list";
    public static final String ADD_CREDIT_OFFER_BUTTON_TEXT = "add credit offer";

    //calculating settings
    public static final int CALCULATE_SCALE = 4;
    public static final int PERCENT_TO_FRACTION_RATE = 100;
    public static final int MONTH_IN_YEAR = 12;
    public static final int MONEY_SCALE = 2;
    public static final int MONTH_MIN_VALUE = 1;

    //text settings
    public static final String PASSPORT_DELIMITER = " / ";
    public static final int NAME_FIELD_MIN_LENGTH = 3;
    public static final int PASSPORT_SERIES_START_INDEX = 0;
    public static final int PASSPORT_NUMBER_START_INDEX = 4;
    public static final int PASSPORT_SERIES_YEAR_START_POSITION = 2;
    public static final int MIN_YEAR_IN_PASSPORT = 90;
    public static final int MAX_YEAR_IN_PASSPORT = 22;
    public static final int PASSPORT_SERIES_MAX_VALUE = 9999;
    public static final int PASSPORT_SERIES_MIN_VALUE = 1000;
    public static final int PASSPORT_NUMBER_MIN_VALUE = 100000;
    public static final int PASSPORT_NUMBER_MAX_VALUE = 999999;

    //texts
    public static final String WEB_SERVER_NAME = "SUPER COOL BANK SYSTEM™";
    public static final String CREATE_NEW_CLIENT_H_3_LABEL = "Create new client";
    public static final String DELETE_TEXT = "Are you really want to delete this?";
    public static final String CREATE_BANK_FORM_NAME_FIELD_LABEL = "enter bank name";
    public static final String CREATE_BANK_FORM_H_3_LABEL = "Create new Bank";
    public static final String ENTER_CUSTOM_MONTH_COUNT_HELPER_TEXT = "you can enter your own number";

    //responses
    public static final String LOG_DELIMITER = ": ";
    public static final String SUCCESSFULLY_SAVED_USER_MESSAGE = "Successfully saved";
    public static final String UPDATING_MESSAGE = "Updating";
    public static final String SUCCESSFULLY_EDITED_USER_MESSAGE = "Edited!";
    public static final String SUCCESSFULLY_DELETED_USER_MESSAGE = "DELETED!";
    public static final String TRYING_TO_SAVE_NEW_BANK = "Trying to save new Bank ";
    public static final String TRYING_TO_SAVE_NEW_CREDIT = "Trying to save new credit ";
    public static final String TRYING_TO_SAVE_NEW_CLIENT = "Trying to save new client";
    public static final String TRYING_TO_EDIT_CREDIT = "trying to edit credit";
    public static final String IS_NULL_OR_BLANK = "is null or blank";
    public static final String IS_VALID = "is valid";
    public static final String IS_NOT_VALID = "is not valid";

    //error texts
    public static final String ERROR_TEXT_DELIMITER = ". ";
    public static final String NULLABLE_ID = "id does not present";
    public static final String WRONG_UUID = "wrong UUID";

    public static final String UUID_IS_ALREADY_USED = "this id is already used";
    public static final String EMPTY_LASTNAME = "lastname field is empty";
    public static final String LASTNAME_INCORRECT_SYMBOLS = "incorrect symbols in lastname";
    public static final String TOO_SHORT_LASTNAME = "lastname min length is 3";
    public static final String EMPTY_NAME = "name field is empty";
    public static final String NAME_INCORRECT_SYMBOLS = "incorrect symbols in firstname";
    public static final String TOO_SHORT_NAME = "name min length is 3";
    public static final String NO_VALID_EMAIL = "email is not valid";
    public static final String NO_VALID_PHONE = "phone is not valid";
    public static final String NO_VALID_PASSPORT = "passport is not valid";
    public static final String EMPTY_PHONE_NUMBER = "phone is required";
    public static final String EMPTY_EMAIL = "email is required";
    public static final String EMAIL_IS_NOT_VALID = "email is not valid";
    public static final String EMPTY_PASSPORT = "passport is empty";
    public static final String PASSPORT_LENGTH_ERROR_MSG =
            "passport must be 10 digits, where third + forth is year of passport receive";
    public static final String PHONE_LENGTH_ERROR_MSG = "phone must be exactly 10 digits";
    public static final String PHONE_PATTERN_ERROR_MSG = "phone must be start with 9";

    //client
    public static final String NULLABLE_CLIENT = "nullable client";
    public static final String CLIENT_DOES_NOT_PRESENT_IN_DB = "this client does not present in db";
    public static final String NO_VALID_CLIENT = "not a valid client";
    public static final String UNIDENTIFIED_CLIENT = "unidentified client";
    public static final String HAVE_ACTIVE_CREDIT_OFFER = "Have active Credit offer";
    //credit
    public static final String NULLABLE_CREDIT = "nullable credit";
    public static final String NO_VALID_CREDIT = "no valid credit";
    public static final String UNIDENTIFIED_CREDIT = "unidentified credit";
    public static final String OLD_CREDIT_IS_NON_PERSIST = "old credit is non persist";
    public static final String ALREADY_UNUSED = "already unused";
    public static final String CREDIT_DOES_NOT_PRESENT_IN_DB = "this credit does not present in db ";
    public static final String NULLABLE_BANK_FIELD = "nullable bank field";
    public static final String NULLABLE_BANK_ID_IN_CREDIT = "nullable bank id in credit";
    //bank
    public static final String BANK_DOES_NOT_EXIST = "bank does not exists";
    public static final String NULLABLE_BANK = "bank is nullable";
    //credit offer
    public static final String NOT_VALID_MONTH_COUNT = "not valid month count";
    public static final String EMPTY_PAYMENT_LIST = "empty payment list";
    public static final String PAYMENTS_IS_NOT_CONNECTED_TO_CREDIT_OFFER = "payments is not connected to credit offer";
    public static final String NOT_VALID_CREDIT_AMOUNT = "not valid credit amount";
    public static final String WRONG_PAYMENTS_COUNT = "wrong payments count";
    public static final String CREDIT_OFFER_DOES_NOT_EXIST = "credit offer does not exist";
    public static final String PAYMENT_PERIOD_IS_ALREADY_START_AND_DOES_NOT_END = "payment period is already start and doesn't end";
    //payment
    public static final String WRONG_INCOME_AMOUNT = "wrong income data - amount in Payment Calculator";
    public static final String WRONG_INCOME_RECEIVING_DATE = "wrong income data - date of receiving in Payment Calculator";

    public static final String WRONG_AMOUNT_MUST_BE_MORE_OR_EQUAL =
            "Wrong credit amount: must be more or equal ";
    public static final String WRONG_RATE_MUST_BE_MORE_OR_EQUAL =
            "Wrong credit rate: must be more or equal ";
    public static final String AND_LESS_THEN = " and less then ";

    public static final String NAME_CAN_NOT_BE_EMPTY = "Name can not be empty";
    public static final String ATTEMPT_TO_SAVE_EMPTY_FIELD = "attempt to save with nullable or empty field";
    public static final String NAME_IS_ALREADY_EXISTS = "This name is already exists ";
    public static final String WRONG_INCOME_DATA = "wrong incoming data";
    public static final String MUST_BE_LETTER_ERROR = "You can use only letter symbols";
    public static final String MUST_BE_MINIMUM_THREE_SYMBOLS_IN_FIRST_NAME = "First name must be minimum three symbols";
    public static final String MUST_BE_MINIMUM_THREE_SYMBOLS_IN_LAST_NAME = "Last name ust be minimum three symbols";
    public static final String MUST_BE_MINIMUM_THREE_SYMBOLS_IN_PATRONYMIC = "Patronymic must be minimum three symbols";
    public static final String MUST_BE_MINIMUM_THREE_SYMBOLS_IN_BANK_NAME = "Bank name must be minimum three symbols";
    public static final String ENUMERATION_ERRORS = "errors: ";
    public static final String THIS_FIELD_IS_REQUIRED_MSG = "this field is required";
    public static final String NO_VALID_PHONE_ERROR_MSG = "not valid number, must be 10 digits starting with 9";
    public static final String PHONE_ALREADY_IN_USE_ERROR_MSG = "this phone is already in use";
    public static final String INCORRECT_EMAIL_ADDRESS_ERROR_MSG = "Incorrect email address";
    public static final String EMAIL_ALREADY_IN_USE_ERROR_MSG = "this email is already in use";
    public static final String PASSPORT_ALREADY_IN_USE_ERROR_MSG = "this passport already in use";
    public static final String MUST_CHOOSE_BANK_ERROR_MSG = "must choose bank";
    public static final String WRONG_CREDIT_LIMIT_MESSAGE = "wrong credit limit value";
    public static final String WRONG_CREDIT_RATE_MESSAGE = "wrong credit rate value";
    public static final String WRONG_MONTH_COUNT_INPUT = "wrong month count";
    public static final String WRONG_DATE_IS_PICKED = "wrong date is picked";
    public static final String MUST_BE_MORE_THAN_ZERO = "must be more then zero";
    public static final String START_DATE_MUST_BE_EARLY_THAN_END_DATE = "start date must be early than end date";
    public static final String CREDIT_RATE_MUST_BE_MORE_OR_EQUAL_ZERO = "credit rate must be more or equal ZERO";
    public static final String CREDIT_REMAIN_BALANCE_MUST_BE_MORE_OR_EQUAL_ZERO = "credit remain balance must be more or equal ZERO";


    //Component settings
    public static final int NOTIFICATION_DURATION = 4000;
    public static final Notification.Position DEFAULT_POSITION = Notification.Position.BOTTOM_CENTER;
    public static final String CLIENT_GRID_LAYOUT_SIZE = "500px";
    public static final String CREDIT_OFFER_GRID_LAYOUT_HEIGHT = "500px";
    public static final String ADD_NEW_CREDIT_OFFER_BUTTON_WIDTH = "10%";
    public static final String ENTITY_DESCRIPTION_MIN_WIDTH = "600px";
    public static final String DELETE_FORM_DIALOG_WIDTH = "40%";


    //labels
    public static final String FIRST_NAME_LABEL = "first name";
    public static final String LAST_NAME_LABEL = "last name";
    public static final String PATRONYMIC_LABEL = "patronymic";
    public static final String PHONE_NUMBER_LABEL = "phone";
    public static final String EMAIL_LABEL = "email";
    public static final String PASSPORT_LABEL = "passport";
    public static final String PASSPORT_SERIES_LABEL = "passport series";
    public static final String PASSPORT_NUMBER_LABEL = "passport number";
    public static final String DATE_LABEL = "date";
    public static final String MAIN_PART_LABEL = "main part";
    public static final String PERCENT_PART_LABEL = "percent part";
    public static final String CLIENT_LABEL = "client";
    public static final String CREDIT_LABEL = "credit";
    public static final String AMOUNT_LABEL = "amount";
    public static final String MONTH_LABEL = "duration in month";
    public static final String BANK_NAME_LABEL = "bank name";
    public static final String BANK_LABEL = "bank";
    public static final String CREDIT_LIMIT_LABEL = "Credit limit, $";
    public static final String CREDIT_RATE_LABEL = "Credit rate, %";
    public static final String RECEIVING_DATE_LABEL = "Date of receiving credit";
    public static final String TOTAL_VALUE = "total value";
    public static final String DOLLAR = "$";
    public static final String BASE_VALUE = "base value";
    public static final String PERCENT_VALUE = "percent value";
    public static final String OLD_CREDIT = "old credit";
    public static final String NEW_CREDIT = "new credit";

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

    //fields naming
    public static final String CLIENT_LAST_NAME_FOR_SORTING = "lastName";
    public static final String CLIENT_FIRST_NAME_FOR_SORTING = "firstName";
    public static final String CLIENT_PATRONYMIC_FOR_SORTING = "patronymic";
    public static final String CLIENT_PHONE_NUMBER_FOR_SORTING = "phoneNumber";
    public static final String CLIENT_EMAIL_FOR_SORTING = "email";
    public static final String CLIENT_PASSPORT_FOR_SORTING = "passport";

    public static final String BANK_NAME_FIELD_NAME = "name";

    public static final String CREDIT_OFFER_AMOUNT_FIELD_NAME = "creditAmount";

    //settings
    public static final boolean IS_SEARCH_FIELD_AUTOSELECTED = true;
    public static final boolean IS_SEARCH_FIELD_AUTOFOCUSED = true;
    public static final boolean IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT = true;
    public static final boolean IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT = true;
    public static final boolean IS_CREATE_CLIENT_FORM_DIALOG_CLOSE_ON_OUTSIDE_CLICK = false;


    public static final String SOME_DB_PROBLEM = "some db problem";
    public static final String UNIVERSAL_NAME_ERROR = "This field is required. Must be minimum three letter without other symbols";
    public static final String PATRONYMIC_ERROR = "This field is not required, cannot be length one or two. Must be use letter only";
    public static final String NO_ERRORS = "No errors";
    public static final String PATTERN_FOR_PATRONYMIC = "^$|[a-zA-z]{3,}";
    public static final String BANK_WITH_TARGET_ID_ALREADY_IN_USE = "bank id is already taken";
    public static final String CREDIT_LIMIT_FIELD_NAME = "creditLimit";
    public static final String CREDIT_RATE_FIELD_NAME = "creditRate";
}
