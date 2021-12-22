package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class PartOfYearCalculatorImplTest {
    @Autowired
    private PartOfYearCalculatorImpl partOfYearCalculator;

    @Test
    void whatPartOfYearIsPeriodBetweenTwoMonthInCommonYear() {
        double actualPart = partOfYearCalculator.whatPartOfYearIsPeriod(
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2010, 2, 1)
        );
        double expectedPart = 31./365;
        Assertions.assertEquals(expectedPart,actualPart);
    }

    @Test
    void whatPartOfYearIsPeriodBetweenTwoMonthInLearYear() {
        double actualPart = partOfYearCalculator.whatPartOfYearIsPeriod(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2000, 2, 1)
        );
        double expectedPart = 31./366;
        Assertions.assertEquals(expectedPart,actualPart);
    }

    @Test
    void whatPartOfYearIsPeriodBetweenDecemberAndJanuary() {
        double actualPart = partOfYearCalculator.whatPartOfYearIsPeriod(
                LocalDate.of(2021, 12, 11),
                LocalDate.of(2022, 1, 11)
        );
        double expectedPart = 31./365;
        Assertions.assertEquals(expectedPart,actualPart);
    }

    @Test
    void whatPartOfYearIsPeriodBetweenDecemberAndJanuaryWhenOneYearIsLeap() {
        double actualPart = partOfYearCalculator.whatPartOfYearIsPeriod(
                LocalDate.of(1999, 12, 11),
                LocalDate.of(2000, 1, 11)
        );
        double expectedPart = 20./365 + 11./366;
        Assertions.assertEquals(expectedPart,actualPart);
    }
}