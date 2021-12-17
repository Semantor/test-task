package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import org.jetbrains.annotations.NotNull;

public interface CreditOfferCreator {
    /**
     * UUID must be not null and not present in db.
     * credit must be not null and present in db.
     * same for client.
     * credit amount must be less than {@link Credit#getCreditLimit()}
     * and more than {@link com.haulmont.testtask.Config#CREDIT_LIMIT_MIN_VALUE}
     *
     * {@link CreditOffer#getPayments()} must be:
     * present
     * connect to credit offer
     * first payment with date >= today
     *
     * all the payment will calculate into this method and compare with {@link CreditOffer#getPayments()}
     * if any fields differ, failed
     *
     * this method also save this payment list.
     *
     * @throws com.haulmont.testtask.backend.excs.CreateCreditOfferException
     */
    void save(@NotNull CreditOffer creditOffer);
}
