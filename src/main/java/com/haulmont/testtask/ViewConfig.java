package com.haulmont.testtask;

import com.haulmont.testtask.backend.*;
import com.haulmont.testtask.view.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ViewConfig {
    @Value("${client.list.default.page.size}")
    private int DEFAULT_PAGE_SIZE;
    @Value("${client.list.default.sort.column}")
    private String DEFAULT_SORT_COLUMN;

    @Bean
    @Scope("prototype")
    ClientGridLayout clientGridLayout(ClientProvider clientProvider, CreditOfferGridLayout creditOfferGridLayout) {
        return new ClientGridLayout(clientProvider, creditOfferGridLayout, DEFAULT_PAGE_SIZE, DEFAULT_SORT_COLUMN);
    }

    @Bean
    @Scope("prototype")
    CreditOfferGridLayout creditOfferGridLayout(CreditOfferProviderByClient creditOfferProviderByClient) {
        return new CreditOfferGridLayout(creditOfferProviderByClient);
    }

    @Bean
    @Scope("prototype")
    PaymentGridLayout paymentGridLayout() {
        return new PaymentGridLayout();
    }

    @Bean
    @Scope("prototype")
    CreateClientForm createClientForm(ClientFieldsValidator clientFieldsValidator, ClientSaver clientSaver) {
        return new CreateClientForm(clientFieldsValidator, clientSaver);
    }

    @Bean
    @Scope("prototype")
    CreateCreditForm createCreditForm(BankProvider bankProvider, CreditSaver creditSaver, Validator validator) {
        return new CreateCreditForm(bankProvider, creditSaver, validator);
    }

    @Bean
    @Scope("prototype")
    CreateCreditOfferForm createCreditOfferForm(CreditOfferCreator creditOfferCreator, CreditProvider creditProvider,
                                                PaymentCalculator paymentCalculator, PaymentGridLayout paymentGridLayout,
                                                Validator validator) {
        return new CreateCreditOfferForm(creditOfferCreator, creditProvider, paymentCalculator, paymentGridLayout, validator);
    }

    @Bean
    @Scope("prototype")
    CreditGridLayout creditGridLayout(CreditProvider creditProvider) {
        return new CreditGridLayout(creditProvider);
    }

    @Bean
    @Scope("prototype")
    CreateBankForm createBankForm(BankSaver bankSaver,BankFieldAvailabilityChecker bankFieldAvailabilityChecker) {
        return new CreateBankForm(bankSaver, bankFieldAvailabilityChecker);
    }

    @Bean
    @Scope("prototype")
    BankGridLayout bankGridLayout(BankProvider bankProvider,
                                  BankClientsGridLayout bankClientsGridLayout) {
        return new BankGridLayout(bankProvider, bankClientsGridLayout);
    }

    @Bean
    @Scope("prototype")
    BankClientsGridLayout bankClientsGridLayout() {
        return new BankClientsGridLayout();
    }

    @Bean
    @Scope("prototype")
    DeleteForm deleteForm(BankRemover bankRemover,
                          CreditRemover creditRemover,
                          CreditOfferRemover creditOfferRemover,
                          ClientRemover clientRemover) {
        return new DeleteForm(bankRemover, creditRemover, creditOfferRemover, clientRemover);
    }

    @Bean
    @Scope("prototype")
    ClientEditorForm clientEditorForm(ClientFieldsValidator clientFieldsValidator,
                                      ClientSaver clientSaver) {
        return new ClientEditorForm(clientFieldsValidator, clientSaver);
    }

    @Bean
    @Scope("prototype")
    CreditEditorForm creditEditorForm(BankProvider bankProvider,
                                      CreditSaver creditSaver,
                                      Validator validator) {
        return new CreditEditorForm(bankProvider, creditSaver, validator);
    }

}
