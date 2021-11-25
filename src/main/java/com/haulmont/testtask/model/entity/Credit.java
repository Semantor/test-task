package com.haulmont.testtask.model.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Table(name = "credits")
public class Credit implements Removable {
    @Id
    @ToString.Exclude
    @Setter(AccessLevel.PROTECTED)
    @Column(name = "credit_id")
    private UUID creditId = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal creditLimit;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal creditRate;

    @Builder
    public Credit(Bank bank, BigDecimal creditLimit, BigDecimal creditRate) {
        this.bank = bank;
        this.creditLimit = creditLimit;
        this.creditRate = creditRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credit credit = (Credit) o;

        return creditId.equals(credit.creditId);
    }

    @Override
    public int hashCode() {
        return creditId.hashCode();
    }

    public String toField() {
        return "Bank{" + bank.getBankId() + "} $" + creditLimit + "/" + creditRate + "%";
    }
}
