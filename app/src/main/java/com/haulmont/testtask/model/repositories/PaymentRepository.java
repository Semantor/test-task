package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
