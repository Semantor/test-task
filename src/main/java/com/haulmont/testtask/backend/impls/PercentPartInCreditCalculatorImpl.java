package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.PartOfYearCalculator;
import com.haulmont.testtask.backend.PercentPartInCreditCalculator;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@RequiredArgsConstructor
public class PercentPartInCreditCalculatorImpl implements PercentPartInCreditCalculator {
    private final PartOfYearCalculator partOfYearCalculator;

    @Override
    public BigDecimal percentPart(LocalDate passedPaymentDate, LocalDate paymentDate, BigDecimal rate, BigDecimal remain) {
        if (!passedPaymentDate.isBefore(paymentDate))
            throw new IllegalArgumentException("start date must be early than end date");
        if (rate.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("credit rate must be more or equal ZERO");
        if (remain.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("credit remain balance must be more or equal ZERO");

        double baseOfCoefficient = Double.parseDouble(rate.toString()) / 100 + 1;
        double exponentOfCoefficient = partOfYearCalculator.whatPartOfYearIsPeriod(passedPaymentDate, paymentDate);

        double percentOfRemaining = Math.pow(baseOfCoefficient, exponentOfCoefficient) - 1.;
        return BigDecimal.valueOf(percentOfRemaining).multiply(remain).setScale(2, RoundingMode.HALF_UP);
    }
}
