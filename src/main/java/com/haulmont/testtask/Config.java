package com.haulmont.testtask;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@SpringBootConfiguration
@PropertySource("classpath:config.properties")
public class Config {

    public static BigDecimal CREDIT_LIMIT_MAX_VALUE;
    public static BigDecimal CREDIT_LIMIT_MIN_VALUE;
    public static BigDecimal CREDIT_RATE_MAX_VALUE;
    public static BigDecimal CREDIT_RATE_MIN_VALUE;

    @Value("${max.credit.limit}")
    private String maxCreditLimit;
    @Value("${min.credit.limit}")
    private String minCreditLimit;
    @Value("${max.credit.rate}")
    private String maxCreditRate;
    @Value("${min.credit.rate}")
    private String minCreditRate;

    @PostConstruct
    private void init() {
        CREDIT_LIMIT_MAX_VALUE = new BigDecimal(maxCreditLimit);
        CREDIT_LIMIT_MIN_VALUE = new BigDecimal(minCreditLimit);
        CREDIT_RATE_MAX_VALUE = new BigDecimal(maxCreditRate);
        CREDIT_RATE_MIN_VALUE = new BigDecimal(minCreditRate);
    }
}
