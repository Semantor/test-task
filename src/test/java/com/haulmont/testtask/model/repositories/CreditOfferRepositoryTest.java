package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.App;
import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfig;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.CreditOffer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {TestConfig.class, App.class, Config.class})
class CreditOfferRepositoryTest {
    @Autowired
    private CreditOfferRepository creditOfferRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void findByIsCanceledAndClient() {
        Client client = clientRepository.findById(UUID.fromString("4209789f-3310-4d16-8074-def0d09ae7b0")).get();
        List<CreditOffer> byIsCanceledAndClient = creditOfferRepository.findByIsCanceledAndClient(true, client);
        List<CreditOffer> byIsNotCanceledAndClient = creditOfferRepository.findByIsCanceledAndClient(false, client);
        assertEquals(3,byIsCanceledAndClient.size());
        assertEquals(0,byIsNotCanceledAndClient.size());
    }
}