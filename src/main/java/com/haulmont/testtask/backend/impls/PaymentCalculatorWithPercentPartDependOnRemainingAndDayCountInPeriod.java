package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.AnnuityCreditCalculator;
import com.haulmont.testtask.backend.PaymentCalculator;
import com.haulmont.testtask.backend.PercentPartInCreditCalculator;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PaymentCalculatorWithPercentPartDependOnRemainingAndDayCountInPeriod implements PaymentCalculator {
    private final AnnuityCreditCalculator annuityCreditCalculator;
    private final Validator validator;
    private final PercentPartInCreditCalculator percentCalculator;

    @Override
    public List<Payment> calculate(Credit credit,
                                   CreditOffer creditOffer,
                                   int month,
                                   BigDecimal amount,
                                   LocalDate dateOfReceiving) throws IllegalArgumentException {
        if (month < 1 || !validator.validateCreditAmount(amount))
            throw new IllegalArgumentException("wrong income data - amount in Payment Calculator");
        if (dateOfReceiving.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("wrong income data - date of receiving in Payment Calculator");

        BigDecimal monthlyPayment = annuityCreditCalculator.calculateMonthlyPayment(amount, credit.getCreditRate(), month);
        List<Payment> result = new ArrayList<>(month);
        BigDecimal remain = amount;
        LocalDate passedPayment = dateOfReceiving;
        LocalDate paymentDate;
        BigDecimal percentPart;
        for (int i = 1; i < month + 1; i++) {
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