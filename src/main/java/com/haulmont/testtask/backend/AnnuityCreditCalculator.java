package com.haulmont.testtask.backend;

import java.math.BigDecimal;

public interface AnnuityCreditCalculator {
    /**
     * All incoming fields must be valid:
     * credit amount more than {@link com.haulmont.testtask.Config#CREDIT_LIMIT_MIN_VALUE} and
     * less than {@link com.haulmont.testtask.Config#CREDIT_LIMIT_MAX_VALUE}
     * interest rate must be more than 0, interest period must be more than 0.
     *
     * @param creditAmount   amount of credit
     * @param yearCreditRateInPercent   credit rate per year
     * @param interestPeriod interest periods until the end of the loan
     * @return monthly payment
     */
    BigDecimal calculateMonthlyPayment(BigDecimal creditAmount, BigDecimal yearCreditRateInPercent, int interestPeriod);
}
