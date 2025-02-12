package com.haulmont.testtask;

import com.haulmont.testtask.backend.AnnuityCreditCalculatorWithRootFormula;
import com.haulmont.testtask.model.repositories.*;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@TestComponent
public class TestConfigForNonJpa {
    @MockBean
    ClientRepository clientRepository;
    @MockBean
    CreditRepository creditRepository;
    @MockBean
    BankRepository bankRepository;
    @MockBean
    CreditOfferRepository creditOfferRepository;
    @MockBean
    PaymentRepository paymentRepository;

    @Bean
    AnnuityCreditCalculatorWithRootFormula annuityCreditCalculatorWithRootFormula() {
        return new AnnuityCreditCalculatorWithRootFormula();
    }

    @Bean
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
