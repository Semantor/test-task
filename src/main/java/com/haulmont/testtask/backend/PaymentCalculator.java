package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * on default credit offer are null and first payment date are the day of creature
 */
public interface PaymentCalculator {
    /**
     * on default credit offer are null and date of receiving is the day of creature
     */
    default List<Payment> calculate(Credit credit, int month, BigDecimal amount) {
        return calculate(credit, month, amount, LocalDate.now());
    }

    /**
     * on default credit offer are null
     */
    default List<Payment> calculate(Credit credit, int month, BigDecimal amount, LocalDate dateOfReceiving) {
        return calculate(credit, null, month, amount, dateOfReceiving);
    }

    /**
     * on default date of receiving is the day of creature
     */
    default List<Payment> calculate(Credit credit, CreditOffer creditOffer, int month, BigDecimal amount) {
        return calculate(credit, creditOffer, month, amount, LocalDate.now());
    }

    List<Payment> calculate(Credit credit, CreditOffer creditOffer, int month, BigDecimal amount, LocalDate dateOfReceiving);

}
