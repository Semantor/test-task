package com.haulmont.testtask.backend;

import java.time.LocalDate;

public interface PartOfYearCalculator {
    /**
     * return what part of year is period between two incoming dates;
     * difference between these dates must be less than year;
     * must be work if dates reside in different years(December - January).
     * must consider if year leap or not.
     *
     * @param passedDate start period date
     * @param nextDate   end period date
     * @return double value of fraction
     */
    double whatPartOfYearIsPeriod(LocalDate passedDate, LocalDate nextDate);

}
