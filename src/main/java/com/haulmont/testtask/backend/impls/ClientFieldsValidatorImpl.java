package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
@AllArgsConstructor
public class ClientFieldsValidatorImpl implements ClientFieldsValidator {
    private final Validator validator;

    @Override
    public boolean validateEmail(String email) {
        log.info("validate email : " + email);
        boolean b = !validator.isNullOrBlank(email);
        boolean matches = b && email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        log.info(matches ? email + " is valid" : email + "is not valid");
        return matches;
    }

    @Override
    public boolean validatePhone(String phone) {
        log.info("validate phone : " + phone);
        boolean b = !validator.isNullOrBlank(phone);
        boolean matches = b && phone.matches("9[0-9]{9}");
        log.info(matches ? phone + " is valid" : phone + "is not valid");
        return matches;
    }

    @Override
    public boolean validatePassport(@NotNull String passport) {
        log.info("validate passport : " + passport);
        boolean b = !validator.isNullOrBlank(passport);
        int i = Integer.parseInt(passport.substring(2, 4));
        boolean matches = b && passport.matches("[1-9][0-9]{9}") && (i < 22 || i > 90);
        log.info(matches ? passport + " is valid" : passport + " is not valid");
        return matches;
    }


}
