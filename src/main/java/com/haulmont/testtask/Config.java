package com.haulmont.testtask;

import com.haulmont.testtask.backend.CreditConstraintProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.math.BigDecimal;

@SpringBootConfiguration
@PropertySource("classpath:config.properties")
public class Config {


    @Value("${max.credit.limit}")
    private BigDecimal maxCreditLimit;
    @Value("${min.credit.limit}")
    private BigDecimal minCreditLimit;
    @Value("${max.credit.rate}")
    private BigDecimal maxCreditRate;
    @Value("${min.credit.rate}")
    private BigDecimal minCreditRate;

    @Bean
    CreditConstraintProvider creditConstraintProvider() {
        return new CreditConstraintProvider(maxCreditLimit, minCreditLimit, maxCreditRate, minCreditRate);
    }
}
