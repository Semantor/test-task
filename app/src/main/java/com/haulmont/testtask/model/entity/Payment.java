package com.haulmont.testtask.model.entity;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.haulmont.testtask.settings.ErrorMessages.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PROTECTED)
@Immutable
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "payment_id")
    @NotNull(message = NULLABLE_ID)
    private UUID paymentId = UUID.randomUUID();

    @Column(name = "date")
    @NotNull(message = WRONG_DATE_IS_PICKED)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_offer_id", nullable = false)
    @NotNull(message = NULLABLE_CREDIT_OFFER)
    @Valid
    private CreditOffer creditOffer;

    @Column(precision = 12, scale = 2, name = "amount")
    private BigDecimal amount;

    @Column(precision = 12, scale = 2, name = "main_part")
    private BigDecimal mainPart;

    @Column(precision = 12, scale = 2, name = "percent_part")
    private BigDecimal percentPart;

    @Builder
    public Payment(LocalDate date, BigDecimal amount, BigDecimal mainPart, BigDecimal percentPart, CreditOffer creditOffer) {
        this.date = date;
        this.amount = amount;
        this.mainPart = mainPart;
        this.percentPart = percentPart;
        this.creditOffer = creditOffer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        return paymentId.equals(payment.paymentId);
    }

    @Override
    public int hashCode() {
        return paymentId.hashCode();
    }

    public String toString() {
        return "Payment(date=" + this.getDate() + ", creditOffer=" + this.getCreditOffer().getCreditOfferId() + ", amount=" + this.getAmount() + ", mainPart=" + this.getMainPart() + ", percentPart=" + this.getPercentPart() + ")";
    }

    public String toSql() {
        return "('" + getPaymentId() + "','" + getDate() + "','" + getCreditOffer().getCreditOfferId() + "',"
                + getAmount() + "," + getMainPart() + "," + getPercentPart() + ")";
    }
}
