package com.haulmont.testtask.model.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.util.UUID;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Credit.class)
public class Credit_ {
    public static volatile SingularAttribute<Credit, UUID> creditId;
    public static volatile SingularAttribute<Credit, Bank> bank;
    public static volatile SingularAttribute<Credit, BigDecimal> creditLimit;
    public static volatile SingularAttribute<Credit, BigDecimal> creditRate;
}
