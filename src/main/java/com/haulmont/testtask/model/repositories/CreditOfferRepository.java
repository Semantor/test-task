package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface CreditOfferRepository extends JpaRepository<CreditOffer, UUID> {

    List<CreditOffer> findByClient(@NotNull Client client);

    List<CreditOffer> findByCredit(@NotNull Credit credit);
}
