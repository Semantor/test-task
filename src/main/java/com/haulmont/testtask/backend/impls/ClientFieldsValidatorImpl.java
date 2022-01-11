package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.view.Hornable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

import static com.haulmont.testtask.Setting.*;

@Slf4j
@AllArgsConstructor
public class ClientFieldsValidatorImpl implements ClientFieldsValidator {
    private final Validator validator;

    public static final String EMAIL_REG_EX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String PHONE_REG_EX = "9[0-9]{9}";
    public static final String PASSPORT_REG_EX = "[1-9][0-9]{9}";


    @Override
    public boolean validateEmail(@Nullable String email) {
        return validate(email, EMAIL_LABEL, EMAIL_REG_EX, () -> true);

    }

    @Override
    public boolean validatePhone(@Nullable String phone) {
        return validate(phone, PHONE_NUMBER_LABEL, PHONE_REG_EX, () -> true);
    }

    @Override
    public boolean validatePassport(@Nullable String passport) {
        return validate(passport, PASSPORT_LABEL, PASSPORT_REG_EX, () -> {
            int i = Integer.parseInt(passport.substring(PASSPORT_SERIES_YEAR_START_POSITION, PASSPORT_NUMBER_START_INDEX));
            return (i <= MAX_YEAR_IN_PASSPORT || i > MIN_YEAR_IN_PASSPORT);
        });
    }

    private boolean validate(@Nullable String fieldValue, String fieldName,
                             String regExMatcher, BooleanSupplier additionalFilter) {
        if (validator.isNullOrBlank(fieldValue)) {
            log.info(Hornable.LOG_TEMPLATE_3, fieldName, LOG_DELIMITER, IS_NULL_OR_BLANK);
            return false;
        }
        if (fieldValue.matches(regExMatcher) && additionalFilter.getAsBoolean()) {
            log.info(Hornable.LOG_TEMPLATE_3, fieldName, LOG_DELIMITER, IS_VALID);
            return true;
        }
        log.info(Hornable.LOG_TEMPLATE_3, fieldName, LOG_DELIMITER, IS_NOT_VALID);
        return false;
    }
}
