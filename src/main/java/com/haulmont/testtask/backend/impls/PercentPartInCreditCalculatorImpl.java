package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.PartOfYearCalculator;
import com.haulmont.testtask.backend.PercentPartInCreditCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static com.haulmont.testtask.Setting.*;

@RequiredArgsConstructor
@Component
public class PercentPartInCreditCalculatorImpl implements PercentPartInCreditCalculator {
    private final PartOfYearCalculator partOfYearCalculator;

  @Override
    public BigDecimal percentPart(LocalDate passedPaymentDate, LocalDate paymentDate, BigDecimal rate, BigDecimal remain) {
        if (!passedPaymentDate.isBefore(paymentDate))
            throw new IllegalArgumentException(START_DATE_MUST_BE_EARLY_THAN_END_DATE);
        if (rate.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException(CREDIT_RATE_MUST_BE_MORE_OR_EQUAL_ZERO);
        if (remain.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException(CREDIT_REMAIN_BALANCE_MUST_BE_MORE_OR_EQUAL_ZERO);

        double baseOfCoefficient = Double.parseDouble(rate.toString()) / PERCENT_TO_FRACTION_RATE + 1;
        double exponentOfCoefficient = partOfYearCalculator.whatPartOfYearIsPeriod(passedPaymentDate, paymentDate);

        double percentOfRemaining = Math.pow(baseOfCoefficient, exponentOfCoefficient) - 1.;
        return BigDecimal.valueOf(percentOfRemaining).multiply(remain).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
