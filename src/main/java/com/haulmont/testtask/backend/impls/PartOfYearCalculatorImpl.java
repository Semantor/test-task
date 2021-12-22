package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.PartOfYearCalculator;

import java.time.LocalDate;
import java.time.Year;

public class PartOfYearCalculatorImpl implements PartOfYearCalculator {

    public double whatPartOfYearIsPeriod(LocalDate passedPaymentDate, LocalDate paymentDate) {
        int dayOfYearOnPaymentDate = paymentDate.getDayOfYear();
        int dayOfYearOnPassedPaymentDate = passedPaymentDate.getDayOfYear();

        if (dayOfYearOnPaymentDate > dayOfYearOnPassedPaymentDate) {
            boolean isLeapYear = Year.isLeap(paymentDate.getYear());
            int periodLength = dayOfYearOnPaymentDate - dayOfYearOnPassedPaymentDate;

            return whatPartOfYearIsPeriod(isLeapYear, periodLength);
        }
        boolean isLeapPassedYear = Year.isLeap(passedPaymentDate.getYear());
        boolean isLeapNewYear = Year.isLeap(paymentDate.getYear());

        int periodLengthInPassedYear = isLeapPassedYear ? 366 - passedPaymentDate.getDayOfYear() : 365 - passedPaymentDate.getDayOfYear();

        double partOfThePassedYear = whatPartOfYearIsPeriod(isLeapPassedYear, periodLengthInPassedYear);
        double partOfNewYear = whatPartOfYearIsPeriod(isLeapNewYear, dayOfYearOnPaymentDate);

        return partOfThePassedYear + partOfNewYear;
    }

    protected double whatPartOfYearIsPeriod(boolean isLeapYear, int periodLengthInDay) {
        return isLeapYear ?
                periodLengthInDay / 366. :
                periodLengthInDay / 365.;
    }
}
