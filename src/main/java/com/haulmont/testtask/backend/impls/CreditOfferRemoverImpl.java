/*
 * Copyright (c) 11/26/21, 2:44 PM.
 * created by fred
 */

package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.CreditOfferRemover;
import com.haulmont.testtask.backend.excs.CreditOfferDeleteException;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.haulmont.testtask.model.repositories.CreditOfferRepository;
import com.haulmont.testtask.model.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@AllArgsConstructor
@Transactional
public class CreditOfferRemoverImpl implements CreditOfferRemover {
    private final CreditOfferRepository creditOfferRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public boolean remove(@Nullable CreditOffer creditOffer) {
        if (creditOffer == null || creditOffer.getCreditOfferId() == null || creditOfferRepository.findById(creditOffer.getCreditOfferId()).isEmpty())
            throw new CreditOfferDeleteException("credit offer does not exists");


        LocalDate now = LocalDate.now();
        boolean isExistsEarlier = false;
        boolean isExistsLater = false;
        for (Payment payment : creditOffer.getPayments()) {
            if (payment.getDate().compareTo(now) < 0) isExistsEarlier = true;
            if (payment.getDate().compareTo(now) > 0) isExistsLater = true;
            if (isExistsEarlier && isExistsLater)
                throw new CreditOfferDeleteException("payment period is already start and doesn't end");
        }

        paymentRepository.deleteAll(creditOffer.getPayments());
        creditOffer.cancel();
        creditOfferRepository.save(creditOffer);
        return true;
    }
}
