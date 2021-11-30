package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.App;
import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import com.haulmont.testtask.backend.AnnuityCreditCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class AnnuityCreditCalculatorImplTest {
    @Autowired
    private AnnuityCreditCalculator annuityCreditCalculator;

    @Test
    void calculateMonthlyPayment() {
        BigDecimal creditAmount = new BigDecimal(20000);
        BigDecimal IR = new BigDecimal("0.01");
        int interestPeriod = 36;
        BigDecimal bigDecimal = annuityCreditCalculator.calculateMonthlyPayment(creditAmount, IR, interestPeriod);
        Assertions.assertEquals(new BigDecimal("664.00"), bigDecimal);
    }
}