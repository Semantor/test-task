package com.haulmont.testtask.model.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.UUID;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Bank.class)
public class Bank_ {
    public static volatile SingularAttribute<Bank, UUID> bankId;
    public static volatile SetAttribute<Bank, Credit> credits;
}
