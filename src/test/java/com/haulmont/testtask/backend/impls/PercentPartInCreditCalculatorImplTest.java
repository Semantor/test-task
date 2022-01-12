package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import com.haulmont.testtask.backend.PartOfYearCalculator;
import com.haulmont.testtask.backend.PercentPartInCreditCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.stream.Stream;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class PercentPartInCreditCalculatorImplTest {
    @MockBean
    private PartOfYearCalculator partOfYearCalculator;

    private PercentPartInCreditCalculator percentPart;

    @BeforeEach
    void init() {
        percentPart = new PercentPartInCreditCalculator(partOfYearCalculator);
    }


    public static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.arguments(
                        LocalDate.of(2010, 4, 1),
                        LocalDate.of(2010, 5, 1),
                        30. / 365,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(99545.88),
                        BigDecimal.valueOf(782.88)
                ),
                Arguments.arguments(
                        LocalDate.of(2029, 10, 1),
                        LocalDate.of(2029, 11, 1),
                        31. / 365,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(2643.54),
                        BigDecimal.valueOf(21.49)
                ),
                Arguments.arguments(
                        LocalDate.of(2029, 12, 1),
                        LocalDate.of(2030, 1, 1),
                        31. / 365,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(805.34),
                        BigDecimal.valueOf(6.55)
                ),
                Arguments.arguments(
                        LocalDate.of(2029, 12, 1),
                        LocalDate.of(2030, 1, 1),
                        31. / 365,
                        BigDecimal.TEN,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                ),
                Arguments.arguments(
                        LocalDate.of(2029, 12, 1),
                        LocalDate.of(2030, 1, 1),
                        31. / 365,
                        BigDecimal.ZERO,
                        BigDecimal.valueOf(805.34),
                        BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    void percentPart(LocalDate start, LocalDate end, double partOfYear, BigDecimal percent, BigDecimal remain, BigDecimal expected) {
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(start, end))
                .thenReturn(partOfYear);
        BigDecimal actualPercentPart = percentPart.percentPart(start, end, percent, remain);
        Assertions.assertEquals(expected, actualPercentPart);
    }

    public static Stream<Arguments> dataForExceptionProvider() {
        return Stream.of(
                Arguments.arguments(
                        LocalDate.of(2030, 1, 1),
                        LocalDate.of(2029, 12, 1),
                        31. / 365,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(805.34)
                ),
                Arguments.arguments(
                        LocalDate.of(2029, 12, 1),
                        LocalDate.of(2029, 12, 1),
                        31. / 365,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(805.34)
                ),
                Arguments.arguments(
                        LocalDate.of(2029, 12, 1),
                        LocalDate.of(2030, 1, 1),
                        31. / 365,
                        BigDecimal.TEN,
                        BigDecimal.valueOf(-805.34)
                ),
                Arguments.arguments(
                        LocalDate.of(2029, 12, 1),
                        LocalDate.of(2030, 1, 1),
                        31. / 365,
                        BigDecimal.TEN.negate(),
                        BigDecimal.valueOf(805.34)
                )

        );
    }

    @ParameterizedTest
    @MethodSource("dataForExceptionProvider")
    void percentPartThrowsIllegalArg(LocalDate start, LocalDate end, double partOfYear, BigDecimal percent, BigDecimal remain) {
        Mockito.when(partOfYearCalculator.whatPartOfYearIsPeriod(start, end))
                .thenReturn(partOfYear);
        Assertions.assertThrows(IllegalArgumentException.class, () -> percentPart.percentPart(
                start, end,
                percent,
                remain));
    }

}