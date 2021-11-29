package com.haulmont.testtask.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "banks")
public class Bank implements Removable{
    @Id
    @Setter(AccessLevel.PROTECTED)
    @Column(name = "bank_id")
    private UUID bankId = UUID.randomUUID();

    @Transient
    @Setter
    private Set<Client> clients;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Credit> credits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        return bankId.equals(bank.bankId);
    }
    @Builder
    public Bank(UUID bankId) {
        this.bankId = bankId;
    }

    @Override
    public int hashCode() {
        return bankId.hashCode();
    }

    @Override
    public String toString() {
        return "Bank{" +
                "uuid=" + bankId +
                '}';
    }
}
