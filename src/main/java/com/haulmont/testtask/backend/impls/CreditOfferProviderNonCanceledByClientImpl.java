package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditOfferProviderByClient;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.model.repositories.CreditOfferRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Component
public class CreditOfferProviderNonCanceledByClientImpl implements CreditOfferProviderByClient {
    private final CreditOfferRepository creditOfferRepository;
    private final ClientRepository clientRepository;

    @Override
    public List<CreditOffer> getByClient(@Nullable Client client) {
        if (client == null || client.getClientId() == null || clientRepository.findById(client.getClientId()).isEmpty())
            return Collections.emptyList();
        return creditOfferRepository.findByIsCanceledAndClient(false, client);
    }
}
