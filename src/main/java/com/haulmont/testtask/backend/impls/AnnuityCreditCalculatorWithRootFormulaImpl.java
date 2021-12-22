package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.AnnuityCreditCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AnnuityCreditCalculatorWithRootFormulaImpl implements AnnuityCreditCalculator {
    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal creditAmount, BigDecimal yearCreditRateInPercent, int interestPeriod) {
        double yearCreditRateDoubleValue = Double.parseDouble(yearCreditRateInPercent.toString()) / 100;
        double exp = 1. / 12;
        double coefficient = Math.pow(yearCreditRateDoubleValue + 1, exp);
        double amountDoubleValue = Double.parseDouble(creditAmount.toString());

        double monthlyPayment =
                amountDoubleValue *
                        Math.pow(coefficient, interestPeriod) *
                        (coefficient - 1) /
                        (Math.pow(coefficient, interestPeriod) - 1);

        return BigDecimal.valueOf(monthlyPayment).setScale(2, RoundingMode.HALF_UP);
    }
}
