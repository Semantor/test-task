package com.haulmont.testtask;

import com.haulmont.testtask.api.ClientsApiController;
import com.haulmont.testtask.api.ClientsApiDelegate;
import com.haulmont.testtask.backend.CreditConstraintProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.math.BigDecimal;

@SpringBootConfiguration
@PropertySource("classpath:application.yml")
public class Config {


    @Value("${max.credit.limit}")
    private BigDecimal maxCreditLimit;
    @Value("${min.credit.limit}")
    private BigDecimal minCreditLimit;
    @Value("${max.credit.rate}")
    private BigDecimal maxCreditRate;
    @Value("${min.credit.rate}")
    private BigDecimal minCreditRate;
    @Value("${credit.month.min}")
    private int monthMinValue;
    @Value("${credit.month.max}")
    private int monthMaxValue;

    @Bean
    CreditConstraintProvider creditConstraintProvider() {
        return new CreditConstraintProvider(maxCreditLimit, minCreditLimit, maxCreditRate, minCreditRate,monthMaxValue,monthMinValue);
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    ClientsApiController clientsApiController(ClientsApiDelegate clientsApiDelegate){
        return new ClientsApiController(clientsApiDelegate);
    }
}
