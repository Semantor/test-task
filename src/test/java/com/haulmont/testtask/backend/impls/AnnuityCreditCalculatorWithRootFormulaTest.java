package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import com.haulmont.testtask.backend.AnnuityCreditCalculatorWithRootFormula;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class AnnuityCreditCalculatorWithRootFormulaTest {
    @Autowired
    private AnnuityCreditCalculatorWithRootFormula annuityCreditCalculatorWithRootFormula;

    @Test
    void calculateMonthlyPayment() {
        BigDecimal actualMonthlyPayment = annuityCreditCalculatorWithRootFormula.calculateMonthlyPayment(new BigDecimal(100_000), BigDecimal.TEN, 240);
        BigDecimal expected = BigDecimal.valueOf(936.64);
        Assertions.assertEquals(expected, actualMonthlyPayment);
    }
}