package com.haulmont.testtask.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Patterns {
    public static final String CREDIT_LIMIT_FORMAT = "$ %(,.2f";
    public static final String CREDIT_RATE_FORMAT = "%(,.2f";
    public static final String ONLY_LETTER_REG_EX = "[a-zA-Z]+";
    public static final String PATTERN_FOR_PATRONYMIC = "^$|[a-zA-z]{3,}";
}
