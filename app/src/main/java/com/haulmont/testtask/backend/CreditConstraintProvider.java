package com.haulmont.testtask.backend;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class CreditConstraintProvider {
    public final BigDecimal CREDIT_LIMIT_MAX_VALUE;
    public final BigDecimal CREDIT_LIMIT_MIN_VALUE;
    public final BigDecimal CREDIT_RATE_MAX_VALUE;
    public final BigDecimal CREDIT_RATE_MIN_VALUE;
    public final int MONTH_MAX_VALUE;
    public final int MONTH_MIN_VALUE;
}
