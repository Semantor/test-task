package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.AnnuityCreditCalculator;
import com.haulmont.testtask.backend.PaymentCalculator;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class PaymentCalculatorImpl implements PaymentCalculator {

    private final AnnuityCreditCalculator annuityCreditCalculator;
    private final Validator validator;

    @Override
    public List<Payment> calculate(Credit credit, int month, BigDecimal amount) {
        return calculate(credit, month, amount, LocalDate.now());
    }

    @Override
    public List<Payment> calculate(Credit credit, int month, BigDecimal amount, LocalDate dateOfReceiving) {
        return calculate(credit, null, month, amount, dateOfReceiving);
    }

    @Override
    public List<Payment> calculate(Credit credit, CreditOffer creditOffer, int month, BigDecimal amount) {
        return calculate(credit, creditOffer, month, amount, LocalDate.now());
    }

    @Override
    public List<Payment> calculate(final Credit credit, final CreditOffer creditOffer, int month, final BigDecimal amount, final LocalDate dateOfReceiving) {
        if (!validator.validateCreditAmount(credit.getCreditLimit()) && !validator.validateCreditRate(credit.getCreditRate()))
            return Collections.emptyList();
        if (!validator.validateCreditAmount(amount)) return Collections.emptyList();
        if (month < 1) return Collections.emptyList();
        BigDecimal monthCount = new BigDecimal(month);
        BigDecimal interestRate = credit.getCreditRate().divide(new BigDecimal(100 * 12), 4, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = annuityCreditCalculator.calculateMonthlyPayment(amount, interestRate, month);

        BigDecimal overprice = monthlyPayment.multiply(monthCount).subtract(amount).divide(monthCount, 2, RoundingMode.HALF_UP);
        BigDecimal main = monthlyPayment.subtract(overprice).setScale(2, RoundingMode.HALF_UP);
        List<Payment> result = new ArrayList<>(month);
        for (int i = 1; i < month+1; i++) {
            LocalDate paymentDate = dateOfReceiving.plus(i, ChronoUnit.MONTHS);
            result.add(Payment.builder()
                    .date(paymentDate)
                    .amount(monthlyPayment)
                    .creditOffer(creditOffer)
                    .percentPart(overprice)
                    .mainPart(main)
                    .build()
            );
        }
        return result;
    }
}
