package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.model.repositories.CreditOfferRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Component
public class CreditOfferProviderByClient {
    private final CreditOfferRepository creditOfferRepository;
    private final ClientRepository clientRepository;

    /**
     * validate client
     * return {@link Collections#emptyList()} on fail
     */
    public List<CreditOffer> getByClient(Client client) {
        List<CreditOffer> clientCreditOffers = Collections.emptyList();
        try {
            if (client.getClientId() == null || clientRepository.findById(client.getClientId()).isEmpty())
                return clientCreditOffers;
            clientCreditOffers = creditOfferRepository.findByIsCanceledAndClient(false, client);
        } catch (Exception ex) {
            return clientCreditOffers;
        }
        return clientCreditOffers;
    }

}
