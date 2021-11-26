package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ALL")
public interface BankRepository extends JpaRepository<Bank, UUID> {
    @Override
    List<Bank> findAll();

    @Override
    Page<Bank> findAll(Pageable pageable);

    @Override
    Optional<Bank> findById(UUID uuid);
}
