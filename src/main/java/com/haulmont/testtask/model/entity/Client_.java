package com.haulmont.testtask.model.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.UUID;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Client.class)
public class Client_ {
    public static volatile SingularAttribute<Client, UUID> clientId;
    public static volatile SingularAttribute<Client, String> firstName;
    public static volatile SingularAttribute<Client, String> lastName;
    public static volatile SingularAttribute<Client, String> patronymic;
    public static volatile SingularAttribute<Client, String> phoneNumber;
    public static volatile SingularAttribute<Client, String> email;
    public static volatile SingularAttribute<Client, String> passport;
    public static volatile ListAttribute<Client, CreditOffer> creditOffers;
    public static volatile SingularAttribute<Client, Boolean> isRemoved;

    public static final String clientIdFieldName = "client_id";
    public static final String firstNameFieldName = "first_name";
    public static final String lastNameFieldName = "last_name";
    public static final String patronymicFieldName = "patronymic";
    public static final String phoneNumberFieldName = "phone_number";
    public static final String emailFieldName = "email";
    public static final String passportFieldName = "passport";
    public static final String creditOffersFieldName = "credit_offers";
    public static final String isRemovedFieldName = "is_removed";

}
