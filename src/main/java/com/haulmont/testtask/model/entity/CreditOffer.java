package com.haulmont.testtask.model.entity;


import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.util.CreditLimitConstraint;
import com.haulmont.testtask.backend.util.CreditOfferLimitConstraint;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "credit_offers")
@CreditOfferLimitConstraint
public class CreditOffer implements Removable {
    @Id
    @Column(name = "credit_offer_id")
    @Setter(AccessLevel.PROTECTED)
    @NotNull(message = Setting.NULLABLE_ID)
    private UUID creditOfferId = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = Setting.NULLABLE_CLIENT)
    @Valid
    private Client client;

    @Column(name = "is_canceled")
    private boolean isCanceled = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_id", nullable = false)
    @NotNull(message = Setting.NULLABLE_CREDIT)
    @Valid
    private Credit credit;

    @Column(name = "credit_amount", nullable = false)
    @CreditLimitConstraint
    private BigDecimal creditAmount;

    @Column(name = "month_count", nullable = false)
    @Min(value = 1, message = Setting.NOT_VALID_MONTH_COUNT)
    @Max(value = 360, message = Setting.NOT_VALID_MONTH_COUNT)
    private int monthCount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creditOffer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @Builder
    public CreditOffer(Client client, Credit credit, BigDecimal creditAmount, int monthCount, List<Payment> payments) {
        this.client = client;
        this.credit = credit;
        this.creditAmount = creditAmount;
        this.monthCount = monthCount;
        if (payments == null) this.payments = new ArrayList<>();
        else this.payments = payments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditOffer that = (CreditOffer) o;

        return creditOfferId.equals(that.creditOfferId);
    }

    @Override
    public int hashCode() {
        return creditOfferId.hashCode();
    }

    @Override
    public String toString() {
        return "CreditOffer(client=" + this.client.getClientId() + ", credit=" + this.getCredit() + ", creditAmount=" + this.getCreditAmount() + ", monthCount=" + this.getMonthCount() + ", payment=" + this.getPayments() + ")";
    }

    public String toSql() {
        return "CreditOffer: ('" + getCreditOfferId() + "','" + getClient().getClientId() + "','" +
                getCredit().getCreditId() + "'," + creditAmount + "," + monthCount +
                ") \n payments: " + payments.stream().reduce("", (s, payment1) -> s + "\n" + payment1.toSql(), String::concat);
    }

    @Override
    public String toDeleteString() {
        return toStringForClient();
    }

    @NotNull
    private String toStringForClient() {
        return "CreditOffer:" +
                "\nClient: " + client.toStringWithoutId() +
                "\n" + this.getCredit().toStringForClient() +
                "\ncredit amount=" + this.getCreditAmount() + ", month count=" + this.getMonthCount();
    }

    public void cancel() {
        isCanceled = true;
        this.payments = Collections.emptyList();
    }
}
