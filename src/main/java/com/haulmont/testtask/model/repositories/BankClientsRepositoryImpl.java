package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Repository
public class BankClientsRepositoryImpl implements BankClientsRepository {
    private final EntityManager entityManager;

    @Override
    public Set<Client> getBankClients(Bank targetBank) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> criteria = cb.createQuery(Client.class);

        Root<Client> client = criteria.from(Client.class);

        Join<Client, CreditOffer> creditOfferJoin = client.join(Client_.creditOffers, JoinType.RIGHT);
        Join<CreditOffer, Credit> creditJoin = creditOfferJoin.join(CreditOffer_.credit, JoinType.RIGHT);
        Join<Credit, Bank> bankJoin = creditJoin.join(Credit_.bank, JoinType.RIGHT);

        Predicate equal = cb.equal(bankJoin.get(Bank_.bankId), targetBank.getBankId());

        criteria.select(client);
        criteria.where(equal);

        TypedQuery<Client> query = entityManager.createQuery(criteria);

        return new HashSet<>(query.getResultList());
    }


}
