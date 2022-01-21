package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static com.haulmont.testtask.settings.BusinessLogicSettings.MONEY_SCALE;
import static com.haulmont.testtask.settings.BusinessLogicSettings.PERCENT_TO_FRACTION_RATE;
import static com.haulmont.testtask.settings.ErrorMessages.*;

@RequiredArgsConstructor
@Component
public class PercentPartInCreditCalculator {
    private final PartOfYearCalculator partOfYearCalculator;

    /**
     * return percent for period between two payments depend on balance on start period date
     *
     * @param startPeriodDate date of the last payment
     * @param endPeriodDate   date of next payment
     * @param rate            credit year rate in percent
     * @param remain          balance on start date
     * @return amount of credit percent tax by using credit in this period.
     */
    public BigDecimal percentPart(LocalDate startPeriodDate, LocalDate endPeriodDate, BigDecimal rate, BigDecimal remain) {
        if (!startPeriodDate.isBefore(endPeriodDate))
            throw new IllegalArgumentExceptionWithoutStackTrace(START_DATE_MUST_BE_EARLY_THAN_END_DATE);
        if (rate.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentExceptionWithoutStackTrace(CREDIT_RATE_MUST_BE_MORE_OR_EQUAL_ZERO);
        if (remain.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentExceptionWithoutStackTrace(CREDIT_REMAIN_BALANCE_MUST_BE_MORE_OR_EQUAL_ZERO);

        double baseOfCoefficient = Double.parseDouble(rate.toString()) / PERCENT_TO_FRACTION_RATE + 1;
        double exponentOfCoefficient = partOfYearCalculator.whatPartOfYearIsPeriod(startPeriodDate, endPeriodDate);

        double percentOfRemaining = Math.pow(baseOfCoefficient, exponentOfCoefficient) - 1.;
        return BigDecimal.valueOf(percentOfRemaining).multiply(remain).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
