package com.haulmont.testtask.backend;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.haulmont.testtask.Setting.*;

/**
 * on default credit offer are null and first payment date are the day of creature
 */
@RequiredArgsConstructor
@Component
public class PaymentCalculatorWithPercentPartDependOnRemainingAndDayCountInPeriod {
    private final AnnuityCreditCalculatorWithRootFormula annuityCreditCalculatorWithRootFormula;
    private final Validator validator;
    private final PercentPartInCreditCalculator percentCalculator;

    /**
     * on default credit offer are null and date of receiving is the day of creature
     */
    public List<Payment> calculate(Credit credit, int month, BigDecimal amount) {
        return calculate(credit, month, amount, LocalDate.now());
    }

    /**
     * on default credit offer are null
     */
    public List<Payment> calculate(Credit credit, int month, BigDecimal amount, LocalDate dateOfReceiving) {
        return calculate(credit, null, month, amount, dateOfReceiving);
    }

    /**
     * on default date of receiving is the day of creature
     */
    public List<Payment> calculate(Credit credit, CreditOffer creditOffer, int month, BigDecimal amount) {
        return calculate(credit, creditOffer, month, amount, LocalDate.now());
    }

    public List<Payment> calculate(Credit credit,
                                   CreditOffer creditOffer,
                                   int month,
                                   BigDecimal amount,
                                   LocalDate dateOfReceiving) throws IllegalArgumentException {
        if (month < MONTH_MIN_VALUE || !validator.validateCreditAmount(amount))
            throw new IllegalArgumentException(WRONG_INCOME_AMOUNT);
        if (dateOfReceiving.isBefore(LocalDate.now()))
            throw new IllegalArgumentException(WRONG_INCOME_RECEIVING_DATE);

        BigDecimal monthlyPayment = annuityCreditCalculatorWithRootFormula.calculateMonthlyPayment(amount, credit.getCreditRate(), month);
        List<Payment> result = new ArrayList<>(month);
        BigDecimal remain = amount;
        LocalDate passedPayment = dateOfReceiving;
        LocalDate paymentDate;
        BigDecimal percentPart;
        for (int i = 1; i < month + 1; i++) {
            if (remain.compareTo(BigDecimal.ZERO)<1)
                    throw new IllegalArgumentExceptionWithoutStackTrace(Setting.TOO_MANY_MONTH_COUNT);
            paymentDate = dateOfReceiving.plus(i, ChronoUnit.MONTHS);
            percentPart = percentCalculator.percentPart(passedPayment, paymentDate, credit.getCreditRate(), remain);
            BigDecimal mainPart = monthlyPayment.subtract(percentPart);
            if (i == month) {
                monthlyPayment = percentPart.add(remain);
                mainPart = remain;
            }
            Payment intermediatePayment = Payment.builder()
                    .date(paymentDate)
                    .creditOffer(creditOffer)
                    .amount(monthlyPayment)
                    .percentPart(percentPart)
                    .mainPart(mainPart)
                    .build();
            result.add(intermediatePayment);
            remain = remain.subtract(mainPart);
            passedPayment = paymentDate;
        }
        return result;
    }

}
