package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.AnnuityCreditCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AnnuityCreditCalculatorImpl implements AnnuityCreditCalculator {

    public static final int CALCULATE_SCALE = 4;
    public static final int PERCENT_TO_FRACTION_RATE = 100;
    public static final int MONTH_IN_YEAR = 12;
    public static final int MONEY_SCALE = 2;


    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal creditAmount, BigDecimal yearCreditRateInPercent, int interestPeriod) {
        BigDecimal interestRate = yearCreditRateInPercent.divide(new BigDecimal(PERCENT_TO_FRACTION_RATE * MONTH_IN_YEAR), CALCULATE_SCALE, RoundingMode.HALF_UP);
        BigDecimal plus = interestRate.add(BigDecimal.ONE);
        BigDecimal divide0 = BigDecimal.ONE.divide(plus, CALCULATE_SCALE, RoundingMode.HALF_UP);
        BigDecimal pow = divide0.pow(interestPeriod);
        BigDecimal subtract = BigDecimal.ONE.subtract(pow);
        BigDecimal divide1 = interestRate.divide(subtract, CALCULATE_SCALE, RoundingMode.HALF_UP);
        BigDecimal multiply = creditAmount.multiply(divide1);
        return multiply.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
