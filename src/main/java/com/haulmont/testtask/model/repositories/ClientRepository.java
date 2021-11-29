package com.haulmont.testtask.model.repositories;

import com.haulmont.testtask.model.entity.Client;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Override
    @NotNull
    Page<Client> findAll(@NotNull Pageable pageable);

    List<Client> findByIsRemoved(boolean isRemoved);

    Page<Client> findByIsRemoved(boolean isRemoved, Pageable pageable);

}
