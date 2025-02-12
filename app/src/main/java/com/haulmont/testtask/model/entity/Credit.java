package com.haulmont.testtask.model.entity;


import com.haulmont.testtask.backend.util.CreditLimitConstraint;
import com.haulmont.testtask.backend.util.CreditRateConstraint;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.haulmont.testtask.settings.ErrorMessages.NULLABLE_BANK;
import static com.haulmont.testtask.settings.ErrorMessages.NULLABLE_ID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "credits")
public class Credit implements Removable {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @NotNull(message = NULLABLE_ID)
    @Column(name = "credit_id")
    private UUID creditId = UUID.randomUUID();

    @Column(name = "is_unused")
    private boolean isUnused = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id", nullable = false)
    @NotNull(message = NULLABLE_BANK)
    @Valid
    private Bank bank;

    @Column(precision = 12, scale = 2, nullable = false)
    @CreditLimitConstraint
    private BigDecimal creditLimit;

    @Column(precision = 4, scale = 2, nullable = false)
    @CreditRateConstraint
    private BigDecimal creditRate;

    @OneToMany(mappedBy = "credit", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter(AccessLevel.PROTECTED)
    private List<CreditOffer> creditOffers;

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
        return "Credit: bank=" + bank.getName().toUpperCase() + " $" + creditLimit + "/" + creditRate + "%";
    }

    @Override
    public String toDeleteString() {
        return toField();
    }

    public void unused() {
        isUnused = true;
    }

    public String toStringForClient() {
        return "Credit: \n" +
                "bank: " + bank.getName() + "\n" +
                "credit limit: " + creditLimit + "\n" +
                "credit rate: " + creditRate;
    }
}
