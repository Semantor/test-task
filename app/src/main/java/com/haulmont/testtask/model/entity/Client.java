package com.haulmont.testtask.model.entity;

import com.haulmont.testtask.backend.util.PassportYearConstraint;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.haulmont.testtask.settings.ErrorMessages.*;
import static com.haulmont.testtask.settings.Patterns.ONLY_LETTER_REG_EX;
import static com.haulmont.testtask.settings.Patterns.PATTERN_FOR_PATRONYMIC;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "clients")
public class Client implements Removable {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @Column(name = "client_id")
    @NotNull(message = NULLABLE_ID)
    private UUID clientId = UUID.randomUUID();

    @Column(name = "is_removed")
    @Setter(AccessLevel.PROTECTED)
    private boolean isRemoved = false;

    @NotNull(message = EMPTY_NAME)
    @Size(min = 3, message = MUST_BE_MINIMUM_THREE_SYMBOLS_IN_FIRST_NAME)
    @Pattern(regexp = ONLY_LETTER_REG_EX, message = MUST_BE_LETTER_ERROR)
    private String firstName;

    @NotNull(message = EMPTY_LASTNAME)
    @Size(min = 3, message = MUST_BE_MINIMUM_THREE_SYMBOLS_IN_LAST_NAME)
    @Pattern(regexp = ONLY_LETTER_REG_EX, message = MUST_BE_LETTER_ERROR)
    private String lastName;

    @Pattern(regexp = PATTERN_FOR_PATRONYMIC, message = PATRONYMIC_ERROR)
    @NotNull
    private String patronymic;

    @NotNull(message = EMPTY_PHONE_NUMBER)
    @Size(min = 10, max = 10, message = PHONE_LENGTH_ERROR_MSG)
    @Pattern(regexp = "9\\d{9}$", message = PHONE_PATTERN_ERROR_MSG)
    @Column(length = 10, unique = true, name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull(message = EMPTY_EMAIL)
    @Email(message = NO_VALID_EMAIL)
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = EMPTY_PASSPORT)
    @Pattern(regexp = "\\d{10}$", message = PASSPORT_LENGTH_ERROR_MSG)
    @Column(unique = true, nullable = false)
    @PassportYearConstraint
    private String passport;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<CreditOffer> creditOffers;

    @Builder
    public Client(String firstName, String lastName, String patronymic, String phoneNumber, String email, String passport) {
        this.firstName = firstName;
        this.lastName = lastName;
        if (patronymic == null) this.patronymic = "";
        else this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passport = passport;
        this.creditOffers = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return clientId.equals(client.clientId);
    }

    public String getStringPassport() {
        return passport.substring(0, 4) + " / " + passport.substring(4);
    }

    @Override
    public int hashCode() {
        return clientId.hashCode();
    }

    public String toField() {
        return lastName + " " + firstName + " " + passport;
    }

    public void remove() {
        isRemoved = true;
    }

    @Override
    public String toDeleteString() {
        return toStringWithoutId();
    }

    public String toStringWithoutId() {
        return lastName + " " + firstName + " " + patronymic + "\n" +
                "phone: " + phoneNumber + "\n" +
                "email: " + email + "\n" +
                "passport: " + getStringPassport();
    }

    public String toString() {
        return "Client(clientId=" + this.getClientId() + ", isRemoved=" + this.isRemoved() + ", firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ", patronymic=" + this.getPatronymic() + ", phoneNumber=" + this.getPhoneNumber() + ", email=" + this.getEmail() + ", passport=" + this.getPassport() + ")";
    }
}
