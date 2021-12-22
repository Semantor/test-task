package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class AnnuityCreditCalculatorImplTest {
    @Autowired
    private AnnuityCreditCalculatorImpl annuityCreditCalculator;

    @Test
    void calculateMonthlyPayment() {
        BigDecimal creditAmount = new BigDecimal(20000);
        BigDecimal yearCreditRate = new BigDecimal(12);
        int interestPeriod = 36;
        BigDecimal monthlyPayment = annuityCreditCalculator.calculateMonthlyPayment(creditAmount, yearCreditRate, interestPeriod);
        Assertions.assertEquals(new BigDecimal("664.00"), monthlyPayment);
    }
}