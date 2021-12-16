package com.haulmont.testtask.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Table(name = "clients")
public class Client implements Removable {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @Column(name = "client_id")
    private UUID clientId = UUID.randomUUID();

    @Column(name = "is_removed")@Setter(AccessLevel.PROTECTED)
    private boolean isRemoved = false;

    private String firstName;

    private String lastName;

    private String patronymic;

    @Size(min = 10, max = 10, message = "Size must be exactly 10 numbers")
    @Pattern(regexp = "9\\d{9}$", message = "string like 9[0123456789]{9}$")
    @Column(length = 10, unique = true, name = "phone_number", nullable = false)
    private String phoneNumber;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 10, max = 10, message = "Size must be exactly 10 numbers")
    @Pattern(regexp = "\\d{10}$", message = "string like [0123456789]{10}$")
    @Column(unique = true, nullable = false)
    private String passport;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    @ToString.Exclude
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

    public String getStringPassport(){
        return passport.substring(0,4) + " / " + passport.substring(4);
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
        return lastName + " " + firstName + "\n" +
                "phone: "+ phoneNumber +"\n"+
                "email: " + email + "\n" +
                "passport: "+ getStringPassport();
    }
}
