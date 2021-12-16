package com.haulmont.testtask;

import com.haulmont.testtask.backend.*;
import com.haulmont.testtask.backend.impls.*;
import com.haulmont.testtask.model.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
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

    @Bean
    AnnuityCreditCalculator annuityCreditCalculator() {
        return new AnnuityCreditCalculatorImpl();
    }

    @Bean
    ClientProvider clientProvider(ClientRepository clientRepository, Validator validator) {
        return new ClientProviderNonRemovedImpl(clientRepository, validator);
    }

    @Bean
    Validator validator() {
        return new Validator();
    }

    @Bean
    CreditOfferProviderByClient creditOfferProvider(CreditOfferRepository creditOfferRepository, ClientRepository clientRepository) {
        return new CreditOfferProviderNonCanceledByClientImpl(creditOfferRepository, clientRepository);
    }

    @Bean
    CreditOfferCreator creditOfferCreator(CreditOfferRepository creditOfferRepository,
                                          CreditRepository creditRepository,
                                          ClientRepository clientRepository,
                                          PaymentCalculator paymentCalculator,
                                          Validator validator,
                                          PaymentRepository paymentRepository) {
        return new CreditOfferCreatorWithExceptionImpl(creditOfferRepository, creditRepository,
                clientRepository, paymentCalculator, validator, paymentRepository);
    }

    @Bean
    ClientFieldsValidator clientValidator(Validator validator) {
        return new ClientFieldsValidatorImpl(validator);
    }

    @Bean
    ClientSaver clientSaver(ClientRepository clientRepository, ClientFieldsValidator clientFieldsValidator, Validator validator) {
        return new ClientSaverWithExceptionImpl(clientFieldsValidator, clientRepository, validator);
    }

    @Bean
    BankProvider bankProvider(BankRepository bankRepository, Validator validator) {
        return new BankProviderImpl(bankRepository, validator);
    }

    @Bean
    BankSaver bankSaver(BankRepository bankRepository) {
        return new BankSaverImpl(bankRepository);
    }

    @Bean
    CreditProvider creditProvider(CreditRepository creditRepository,
                                  Validator validator) {
        return new CreditProviderNonUnusedImpl(creditRepository, validator);
    }

    @Bean
    CreditSaver creditSaver(CreditRepository creditRepository, BankRepository bankRepository, Validator validator) {
        return new CreditSaverImpl(creditRepository, bankRepository, validator);
    }

    @Bean
    PaymentCalculator paymentCalculator(AnnuityCreditCalculator annuityCreditCalculator,
                                        Validator validator) {
        return new PaymentCalculatorImpl(annuityCreditCalculator, validator);
    }

    @Bean
    BankRemover bankRemover(BankRepository bankRepository) {
        return new BankRemoverImpl(bankRepository);
    }

    @Bean
    CreditRemover creditRemover(CreditRepository creditRepository) {
        return new CreditRemoverImpl(creditRepository);
    }

    @Bean
    ClientRemover clientRemover(ClientRepository clientRepository) {
        return new ClientRemoverImpl(clientRepository);
    }

    @Bean
    CreditOfferRemover creditOfferRemover(CreditOfferRepository creditOfferRepository,
                                          PaymentRepository paymentRepository) {
        return new CreditOfferRemoverImpl(creditOfferRepository, paymentRepository);
    }

    @Bean
    CreditEditService creditEditService(CreditRepository creditRepository) {
        return new CreditEditServiceImpl(creditRepository);
    }

}
