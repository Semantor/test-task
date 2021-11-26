package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public interface CreditOfferProviderByClient {

    /**
     * validate client
     * return {@link Collections#emptyList()} on fail
     */
    List<CreditOffer> getByClient(@Nullable Client client);

}
