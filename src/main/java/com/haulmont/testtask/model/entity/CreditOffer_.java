package com.haulmont.testtask.model.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.util.UUID;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CreditOffer.class)
public class CreditOffer_ {
    public static volatile SingularAttribute<CreditOffer, UUID> creditOfferId;
    public static volatile SingularAttribute<CreditOffer, Client> client;
    public static volatile SingularAttribute<CreditOffer, Credit> credit;
    public static volatile SingularAttribute<CreditOffer, BigDecimal> creditAmount;
    public static volatile SingularAttribute<CreditOffer, Integer> monthCount;
    public static volatile ListAttribute<CreditOffer, Payment> payment;
    public static volatile SingularAttribute<CreditOffer, Boolean> isCanceled;

}
