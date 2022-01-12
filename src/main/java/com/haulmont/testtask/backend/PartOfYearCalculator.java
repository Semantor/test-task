package com.haulmont.testtask.backend;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PartOfYearCalculator {
    /**
     * return what part of year is period between two incoming dates;
     * difference between these dates must be less than year;
     * must be work if dates reside in different years(December - January).
     * must consider if year leap or not.
     *
     * @param startPeriodDate start period date
     * @param endPeriodDate   end period date
     * @return double value of fraction
     */
    public double whatPartOfYearIsPeriod(LocalDate startPeriodDate, LocalDate endPeriodDate) {
        int dayOfYearOnPaymentDate = endPeriodDate.getDayOfYear();
        int dayOfYearOnPassedPaymentDate = startPeriodDate.getDayOfYear();

        if (dayOfYearOnPaymentDate > dayOfYearOnPassedPaymentDate) {
            int periodLength = dayOfYearOnPaymentDate - dayOfYearOnPassedPaymentDate;
            int yearLength = startPeriodDate.lengthOfYear();
            return periodLength / (double) yearLength;
        }
        int periodLengthInPassedYear = startPeriodDate.lengthOfYear() - startPeriodDate.getDayOfYear();

        double partOfThePassedYear = periodLengthInPassedYear / (double) startPeriodDate.lengthOfYear();
        double partOfNewYear = endPeriodDate.getDayOfYear() / (double) endPeriodDate.lengthOfYear();

        return partOfThePassedYear + partOfNewYear;
    }

}
