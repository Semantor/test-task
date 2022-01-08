package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

import static com.haulmont.testtask.model.entity.Client_.*;

@Slf4j
@AllArgsConstructor
public class ClientFieldsValidatorImpl implements ClientFieldsValidator {
    private final Validator validator;

    public static final String EMAIL_REG_EX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String PHONE_REG_EX = "9[0-9]{9}";
    public static final String PASSPORT_REG_EX = "[1-9][0-9]{9}";


    @Override
    public boolean validateEmail(@Nullable String email) {
        return validate(email, emailFieldName, EMAIL_REG_EX, () -> true);

    }

    @Override
    public boolean validatePhone(@Nullable String phone) {
        return validate(phone, phoneNumberFieldName, PHONE_REG_EX, () -> true);
    }

    @Override
    public boolean validatePassport(@Nullable String passport) {
        return validate(passport, passportFieldName, PASSPORT_REG_EX, () -> {
            int i = Integer.parseInt(passport.substring(2, 4));
            return (i < 22 || i > 90);
        });
    }

    private boolean validate(@Nullable String fieldValue, String fieldName,
                             String regExMatcher, BooleanSupplier additionalFilter) {
        if (validator.isNullOrBlank(fieldValue)) {
            log.info(fieldName + " is null or blank");
            return false;
        }
        if (fieldValue.matches(regExMatcher) && additionalFilter.getAsBoolean()) {
            log.info(fieldName + ": " + fieldName + " is valid");
            return true;
        }
        log.info(fieldName + ": " + fieldName + " is not valid");
        return false;
    }
}
