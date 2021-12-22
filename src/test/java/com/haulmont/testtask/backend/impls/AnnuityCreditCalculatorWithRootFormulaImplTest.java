package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class AnnuityCreditCalculatorWithRootFormulaImplTest {
    @Autowired
    private AnnuityCreditCalculatorWithRootFormulaImpl calculator;

    @Test
    void calculateMonthlyPayment() {
        BigDecimal actualMonthlyPayment = calculator.calculateMonthlyPayment(new BigDecimal(100_000), BigDecimal.TEN, 240);
        BigDecimal expected = BigDecimal.valueOf(936.64);
        Assertions.assertEquals(expected, actualMonthlyPayment);
    }
}