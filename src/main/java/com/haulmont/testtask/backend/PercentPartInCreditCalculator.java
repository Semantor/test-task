package com.haulmont.testtask.backend;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PercentPartInCreditCalculator {
    /**
     * return percent for period between two payments depend on balance on start period date
     *
     * @param startPeriodDate date of the last payment
     * @param endPeriodDate   date of next payment
     * @param rate            credit year rate in percent
     * @param remain          balance on start date
     * @return amount of credit percent tax by using credit in this period.
     *
     * @throws IllegalArgumentException on wrong incoming date, fe startDate > endDate, negative balance, negative rate.
     */
    BigDecimal percentPart(LocalDate startPeriodDate, LocalDate endPeriodDate, BigDecimal rate, BigDecimal remain);
}
