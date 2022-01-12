package com.haulmont.testtask.backend;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.haulmont.testtask.Setting.MONEY_SCALE;
import static com.haulmont.testtask.Setting.MONTH_IN_YEAR;

@Component
public class AnnuityCreditCalculatorWithRootFormula {
    /**
     * All incoming fields must be valid:
     * credit amount more than {@link com.haulmont.testtask.backend.CreditConstraintProvider#CREDIT_LIMIT_MIN_VALUE} and
     * less than {@link com.haulmont.testtask.backend.CreditConstraintProvider#CREDIT_LIMIT_MAX_VALUE}
     * interest rate must be more than 0, interest period must be more than 0.
     *
     * @param creditAmount            amount of credit
     * @param yearCreditRateInPercent credit rate per year
     * @param interestPeriod          interest periods until the end of the loan
     * @return monthly payment
     */
    public BigDecimal calculateMonthlyPayment(BigDecimal creditAmount, BigDecimal yearCreditRateInPercent, int interestPeriod) {
        double yearCreditRateDoubleValue = Double.parseDouble(yearCreditRateInPercent.toString()) / 100;
        double exp = 1. / MONTH_IN_YEAR;
        double coefficient = Math.pow(yearCreditRateDoubleValue + 1, exp);
        double amountDoubleValue = Double.parseDouble(creditAmount.toString());

        double monthlyPayment =
                amountDoubleValue *
                        Math.pow(coefficient, interestPeriod) *
                        (coefficient - 1) /
                        (Math.pow(coefficient, interestPeriod) - 1);

        return BigDecimal.valueOf(monthlyPayment).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
