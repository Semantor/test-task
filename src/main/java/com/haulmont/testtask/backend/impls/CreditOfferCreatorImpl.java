package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditOfferCreator;
import com.haulmont.testtask.backend.PaymentCalculator;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.model.repositories.CreditOfferRepository;
import com.haulmont.testtask.model.repositories.CreditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class CreditOfferCreatorImpl implements CreditOfferCreator {
    private final CreditOfferRepository creditOfferRepository;
    private final CreditRepository creditRepository;
    private final ClientRepository clientRepository;
    private final PaymentCalculator paymentCalculator;
    private final Validator validator;

    @Override
    public void save(@NotNull final CreditOffer creditOffer) {
        if (creditOffer.getCreditOfferId() == null)
            return;
        if (creditOffer.getClient() == null || creditOffer.getClient().getClientId() == null) return;
        Optional<Client> clientOpt = clientRepository.findById(creditOffer.getClient().getClientId());
        if (clientOpt.isEmpty()) return;
        creditOffer.setClient(clientOpt.get());
        if (creditOffer.getCredit() == null || creditOffer.getCredit().getCreditId() == null) return;
        Optional<Credit> creditOpt = creditRepository.findById(creditOffer.getCredit().getCreditId());
        if (creditOpt.isEmpty()) return;
        if (creditOffer.getCreditAmount() == null ||
                !validator.validateCreditAmount(creditOffer.getCreditAmount(), creditOpt.get().getCreditLimit()))
            return;
        creditOffer.setCredit(creditOpt.get());
        if (creditOffer.getMonthCount() < 1) return;
        if (creditOffer.getPayments() == null || creditOffer.getPayments().isEmpty()) return;

        boolean b = false;
        for (Payment payment : creditOffer.getPayments()) {
            if (!payment.getCreditOffer().equals(creditOffer)) {
                b = true;
                break;
            }
        }
        if (b) {
            log.warn("payments is not connected to credit offer");
        }
        if (!paymentCalculator.calculate(creditOffer.getCredit(), creditOffer.getMonthCount(), creditOffer.getCreditAmount())
                .equals(creditOffer.getPayments())) {
            log.warn("payment list in credit offer is not predictable");
            return;
        }
        log.info("save new credit offer: " + creditOffer);
        creditOfferRepository.save(creditOffer);
    }

}
