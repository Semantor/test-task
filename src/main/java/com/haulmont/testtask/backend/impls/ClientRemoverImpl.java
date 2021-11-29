/*
 * Copyright (c) 11/26/21, 2:15 PM.
 * created by fred
 */

package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientRemover;
import com.haulmont.testtask.backend.excs.ClientDeleteException;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.haulmont.testtask.model.entity.Removable;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class ClientRemoverImpl implements ClientRemover {
    private final ClientRepository clientRepository;

    @Override
    public boolean remove(Client client) {
        if (client == null || client.getClientId() == null || clientRepository.findById(client.getClientId()).isEmpty())
            throw new ClientDeleteException(ClientDeleteException.NON_VALID);
        LocalDate now = LocalDate.now();
        for (CreditOffer creditOffer : client.getCreditOffers()) {
            for (Payment payment : creditOffer.getPayments()) {
                if (payment.getDate().compareTo(now) > -1)
                    throw new ClientDeleteException(ClientDeleteException.HAVE_ACTIVE_CREDIT_OFFER);
            }
        }

        client.remove();
        clientRepository.save(client);
        return true;
    }

    @Override
    public boolean remove(Removable removable) {
        return remove((Client) removable);
    }
}
