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
     * on default credit offer are null and first payment date are the day of creature
     */
    List<Payment> calculate(Credit credit, int month, BigDecimal amount);

    /**
     * on default credit offer are null
     */
    List<Payment> calculate(Credit credit, int month, BigDecimal amount, LocalDate firstPaymentDate);

    /**
     * on default first payment date are the day of creature
     */
    List<Payment> calculate(Credit credit, CreditOffer creditOffer, int month, BigDecimal amount);

    List<Payment> calculate(Credit credit, CreditOffer creditOffer, int month, BigDecimal amount, LocalDate firstPaymentDate);

}
