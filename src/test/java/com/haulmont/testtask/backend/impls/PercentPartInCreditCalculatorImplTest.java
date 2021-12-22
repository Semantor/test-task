package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import com.haulmont.testtask.backend.PartOfYearCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class PercentPartInCreditCalculatorImplTest {
    @MockBean
    PartOfYearCalculator partOfYearCalculator;
    @Autowired
    private PercentPartInCreditCalculatorImpl percentPart;

    @Test
    void percentPartFirst() {
        LocalDate startDate = LocalDate.of(2010, 4, 1);
        LocalDate endDate = LocalDate.of(2010, 5, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(30. / 365);
        BigDecimal actualPercentPart = percentPart.percentPart(
                startDate, endDate,
                BigDecimal.TEN,
                BigDecimal.valueOf(99545.88));
        BigDecimal expected = BigDecimal.valueOf(782.88);
        Assertions.assertEquals(expected, actualPercentPart);
    }

    @Test
    void percentPartSecond() {
        LocalDate startDate = LocalDate.of(2029, 10, 1);
        LocalDate endDate = LocalDate.of(2029, 11, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(31. / 365);
        BigDecimal actualPercentPart = percentPart.percentPart(
                startDate, endDate,
                BigDecimal.TEN,
                BigDecimal.valueOf(2643.54));
        BigDecimal expected = BigDecimal.valueOf(21.49);
        Assertions.assertEquals(expected, actualPercentPart);
    }

    @Test
    void percentPartThird() {
        LocalDate startDate = LocalDate.of(2029, 12, 1);
        LocalDate endDate = LocalDate.of(2030, 1, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(31. / 365);
        BigDecimal actualPercentPart = percentPart.percentPart(
                startDate, endDate,
                BigDecimal.TEN,
                BigDecimal.valueOf(805.34));
        BigDecimal expected = BigDecimal.valueOf(6.55);
        Assertions.assertEquals(expected, actualPercentPart);
    }

    @Test
    void percentPartWithZeroAmount() {
        LocalDate startDate = LocalDate.of(2029, 12, 1);
        LocalDate endDate = LocalDate.of(2030, 1, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(31. / 365);
        BigDecimal actualPercentPart = percentPart.percentPart(
                startDate, endDate,
                BigDecimal.TEN,
                BigDecimal.ZERO);
        BigDecimal expected = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        Assertions.assertEquals(expected, actualPercentPart);
    }

    @Test
    void percentPartWithZeroRate() {
        LocalDate startDate = LocalDate.of(2029, 12, 1);
        LocalDate endDate = LocalDate.of(2030, 1, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(31. / 365);
        BigDecimal actualPercentPart = percentPart.percentPart(
                startDate, endDate,
                BigDecimal.ZERO,
                BigDecimal.valueOf(805.34));
        BigDecimal expected = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        Assertions.assertEquals(expected, actualPercentPart);
    }

    @Test
    void percentPartThrowsIllegalArgExWhenEndDateBeforeStartDate() {
        LocalDate endDate = LocalDate.of(2029, 12, 1);
        LocalDate startDate = LocalDate.of(2030, 1, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(31. / 365);
        BigDecimal remain = BigDecimal.valueOf(805.34);
        Assertions.assertThrows(IllegalArgumentException.class, () -> percentPart.percentPart(
                startDate, endDate,
                BigDecimal.TEN,
                remain));
    }

    @Test
    void percentPartThrowsIllegalArgExWhenStartDateAndEndDateIsSameDate() {
        LocalDate startDate = LocalDate.of(2029, 12, 1);
        LocalDate endDate = LocalDate.of(2029, 12, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(31. / 365);
        BigDecimal remain = BigDecimal.valueOf(805.34);
        Assertions.assertThrows(IllegalArgumentException.class, () -> percentPart.percentPart(
                startDate, endDate,
                BigDecimal.TEN,
                remain));
    }

    @Test
    void percentPartThrowsIllegalArgExWithNegativeBalance() {
        LocalDate startDate = LocalDate.of(2029, 12, 1);
        LocalDate endDate = LocalDate.of(2030, 1, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(31. / 365);
        BigDecimal remain = BigDecimal.valueOf(-805.34);
        Assertions.assertThrows(IllegalArgumentException.class, () -> percentPart.percentPart(
                startDate, endDate,
                BigDecimal.TEN,
                remain));
    }

    @Test
    void percentPartThrowsIllegalArgExWithNegativeCreditRate() {
        LocalDate startDate = LocalDate.of(2029, 12, 1);
        LocalDate endDate = LocalDate.of(2030, 1, 1);
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(startDate, endDate))
                .thenReturn(31. / 365);
        BigDecimal remain = BigDecimal.valueOf(805.34);
        BigDecimal creditRate = BigDecimal.TEN.negate();
        Assertions.assertThrows(IllegalArgumentException.class, () -> percentPart.percentPart(
                startDate, endDate,
                creditRate,
                remain));
    }
}