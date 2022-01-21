package com.haulmont.testtask.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessLogicSettings {
    public static final int CALCULATE_SCALE = 4;
    public static final int PERCENT_TO_FRACTION_RATE = 100;
    public static final int MONTH_IN_YEAR = 12;
    public static final int MONEY_SCALE = 2;

    public static final int MIN_YEAR_IN_PASSPORT = 91;
    public static final int MAX_YEAR_IN_PASSPORT = LocalDate.now().getYear()%100;
}
