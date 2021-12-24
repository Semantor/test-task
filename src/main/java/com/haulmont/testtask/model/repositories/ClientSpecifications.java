package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.Client_;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecifications {

    public Specification<Client> getByPhoneExactMatch(String phone) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Client_.phoneNumber), phone);
    }

    public Specification<Client> getByEmailExactMatch(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Client_.email), email);
    }

    public Specification<Client> getByPassportExactMatch(String passport) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Client_.passport), passport);
    }

}
