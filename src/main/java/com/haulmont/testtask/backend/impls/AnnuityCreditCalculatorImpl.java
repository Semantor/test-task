package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.AnnuityCreditCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AnnuityCreditCalculatorImpl implements AnnuityCreditCalculator {
    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal creditAmount, BigDecimal interestRate, int interestPeriod) {
        BigDecimal plus = interestRate.add(BigDecimal.ONE);
        BigDecimal divide0 = BigDecimal.ONE.divide(plus, 4, RoundingMode.HALF_UP);
        BigDecimal pow = divide0.pow(interestPeriod);
        BigDecimal subtract = BigDecimal.ONE.subtract(pow);
        BigDecimal divide1 = interestRate.divide(subtract, 4, RoundingMode.HALF_UP);
        BigDecimal multiply = creditAmount.multiply(divide1);
        return multiply.setScale(2, RoundingMode.HALF_UP);
    }
}
