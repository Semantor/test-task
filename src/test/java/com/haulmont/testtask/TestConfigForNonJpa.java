package com.haulmont.testtask;

import com.haulmont.testtask.model.repositories.*;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;

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
}
