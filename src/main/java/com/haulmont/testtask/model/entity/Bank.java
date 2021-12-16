package com.haulmont.testtask.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "banks")
@Setter(AccessLevel.PROTECTED)
@ToString
public class Bank implements Removable {
    @Id
    @Column(name = "bank_id")
    private UUID bankId = UUID.randomUUID();

    @Column(name = "bank_name")
    private String name;

    @Transient
    @Setter
    @ToString.Exclude
    private Set<Client> clients;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private Set<Credit> credits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        return bankId.equals(bank.bankId);
    }

    @Builder
    public Bank(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return bankId.hashCode();
    }


    public String toField() {
        return "Bank(name=" + this.getName() + ")";
    }

    @Override
    public String toDeleteString() {
        return toField();
    }
}
