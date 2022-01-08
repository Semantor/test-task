package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.Client_;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecifications {

    public Specification<Client> searchClientByAllField(String keyword){
        return getByFirstNameLike(keyword)
                .or(getByLastNameLike(keyword))
                .or(getByPatronymicLike(keyword))
                .or(getByPhoneLike(keyword))
                .or(getByEmailLike(keyword))
                .or(getByPassportLike(keyword));

    }

    public Specification<Client> searchNonRemovedClientByAllField(String keyword){
        return searchClientByAllField(keyword).and(getNonRemoved());
    }

    public Specification<Client> getByPhoneExactMatch(String phone) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Client_.phoneNumber), phone);
    }

    public Specification<Client> getByEmailExactMatch(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Client_.email), email);
    }

    public Specification<Client> getByPassportExactMatch(String passport) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Client_.passport), passport);
    }

    public Specification<Client> getByFirstNameLike(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Client_.firstName), "%"+firstName+"%");
    }

    public Specification<Client> getByLastNameLike(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Client_.lastName), "%"+lastName+"%");
    }

    public Specification<Client> getByPatronymicLike(String patronymic) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Client_.patronymic), "%"+patronymic+"%");
    }

    public Specification<Client> getByPhoneLike(String phone) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Client_.phoneNumber), "%"+phone+"%");
    }

    public Specification<Client> getByEmailLike(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Client_.email), "%"+email+"%");
    }

    public Specification<Client> getByPassportLike(String passport) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Client_.passport), "%"+passport+"%");
    }

    public Specification<Client> getNonRemoved() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Client_.isRemoved), false);
    }
}
