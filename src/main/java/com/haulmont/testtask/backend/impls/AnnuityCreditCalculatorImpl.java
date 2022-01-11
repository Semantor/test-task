package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.AnnuityCreditCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.haulmont.testtask.Setting.*;

public class AnnuityCreditCalculatorImpl implements AnnuityCreditCalculator {


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
