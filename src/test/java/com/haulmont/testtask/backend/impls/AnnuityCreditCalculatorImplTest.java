package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.AnnuityCreditCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class AnnuityCreditCalculatorImplTest {
private final AnnuityCreditCalculator annuityCreditCalculator= new AnnuityCreditCalculatorImpl();
    @Test
    void calculateMonthlyPayment() {
        BigDecimal creditAmount = new BigDecimal(20000);
        BigDecimal IR = new BigDecimal("0.01");
        int interestPeriod = 36;
        BigDecimal bigDecimal = annuityCreditCalculator.calculateMonthlyPayment(creditAmount, IR, interestPeriod);
        Assertions.assertEquals(new BigDecimal("664.00"),bigDecimal);
    }
}