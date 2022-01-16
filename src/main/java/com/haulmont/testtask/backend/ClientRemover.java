package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.excs.Result;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.haulmont.testtask.model.entity.Removable;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.haulmont.testtask.Setting.HAVE_ACTIVE_CREDIT_OFFER;
import static com.haulmont.testtask.Setting.NO_VALID_CLIENT;

@AllArgsConstructor
@Component
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ClientRemover implements Remover {
    private final ClientRepository clientRepository;

    /**
     * trying to make client {@link Client#isRemoved()}
     */
    public Result<Boolean> remove(Client client) {
        try {
            if (client.getClientId() == null || clientRepository.findById(client.getClientId()).isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(NO_VALID_CLIENT);
            LocalDate now = LocalDate.now();
            for (CreditOffer creditOffer : client.getCreditOffers()) {
                for (Payment payment : creditOffer.getPayments()) {
                    if (payment.getDate().compareTo(now) > -1) {
                        throw new IllegalArgumentExceptionWithoutStackTrace(HAVE_ACTIVE_CREDIT_OFFER);
                    }
                }
            }
            client.remove();
            clientRepository.save(client);
        } catch (Exception ex) {
            return Result.failure(ex);
        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean> remove(Removable removable) {
        return remove((Client) removable);
    }
}
