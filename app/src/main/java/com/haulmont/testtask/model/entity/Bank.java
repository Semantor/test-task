package com.haulmont.testtask.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

import static com.haulmont.testtask.settings.ErrorMessages.*;
import static com.haulmont.testtask.settings.Patterns.ONLY_LETTER_REG_EX;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "banks")
@Setter(AccessLevel.PROTECTED)
@ToString
public class Bank implements Removable {
    @Id
    @Column(name = "bank_id")
    @NotNull(message = NULLABLE_ID)
    private UUID bankId = UUID.randomUUID();

    @Column(name = "bank_name")
    @NotNull(message = EMPTY_NAME)
    @Size(min = 3, message = MUST_BE_MINIMUM_THREE_SYMBOLS_IN_BANK_NAME)
    @Pattern(regexp = ONLY_LETTER_REG_EX, message = MUST_BE_LETTER_ERROR)
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
