package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {

    List<Credit> findByIsUnused(boolean isUnused);
}
