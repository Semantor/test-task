package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.excs.ClientDeleteException;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.haulmont.testtask.model.entity.Removable;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.haulmont.testtask.Setting.HAVE_ACTIVE_CREDIT_OFFER;
import static com.haulmont.testtask.Setting.NO_VALID_CLIENT;

@AllArgsConstructor
@Component
public class ClientRemover implements Remover {
    private final ClientRepository clientRepository;

    /**
     * trying to make client {@link Client#isRemoved()}
     *
     * @param client that is being removed
     * @return true if successful removed
     * @throws com.haulmont.testtask.backend.excs.ClientDeleteException due to failed, fe client has active credit offer
     */
    public boolean remove(Client client) {
        if (client == null || client.getClientId() == null || clientRepository.findById(client.getClientId()).isEmpty())
            throw new ClientDeleteException(NO_VALID_CLIENT);
        LocalDate now = LocalDate.now();
        for (CreditOffer creditOffer : client.getCreditOffers()) {
            for (Payment payment : creditOffer.getPayments()) {
                if (payment.getDate().compareTo(now) > -1)
                    throw new ClientDeleteException(HAVE_ACTIVE_CREDIT_OFFER);
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
