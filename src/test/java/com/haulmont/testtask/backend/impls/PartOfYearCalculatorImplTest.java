package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import com.haulmont.testtask.backend.PartOfYearCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class PartOfYearCalculatorImplTest {

    private final PartOfYearCalculator partOfYearCalculator
            = new PartOfYearCalculator();
    private final double scale = 0.00000001;

    public static Stream<Arguments> periodProvider() {
        return Stream.of(
                Arguments.arguments(LocalDate.of(2010, 1, 1),
                        LocalDate.of(2010, 2, 1),
                        31. / 365),
                Arguments.arguments(LocalDate.of(2000, 1, 1),
                        LocalDate.of(2000, 2, 1),
                        31. / 366),
                Arguments.arguments(LocalDate.of(2021, 12, 11),
                        LocalDate.of(2022, 1, 11),
                        31. / 365),
                Arguments.arguments(LocalDate.of(1999, 12, 11),
                        LocalDate.of(2000, 1, 11),
                        20. / 365 + 11. / 366)
        );
    }

    @ParameterizedTest
    @MethodSource("periodProvider")
    void whatPartOfYearIsPeriod(LocalDate start, LocalDate end, double expected) {
        double actualPart = partOfYearCalculator.whatPartOfYearIsPeriod(start, end);
        Assertions.assertTrue(Math.abs(expected - actualPart) < scale);
    }
}