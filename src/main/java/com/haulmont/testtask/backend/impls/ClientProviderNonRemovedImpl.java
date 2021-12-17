package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientProvider;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class ClientProviderNonRemovedImpl implements ClientProvider {
    private final ClientRepository clientRepository;
    private final Validator validator;

    @Override
    public List<Client> getClients() {
        return clientRepository.findByIsRemoved(false);
    }

    @Override
    public List<Client> getClients(int pageSize, int pageNumber, @NotNull String sort) {
        if (pageSize < 1 || pageNumber < 0) return Collections.emptyList();
        return clientRepository.findByIsRemoved(false, PageRequest.of(pageNumber, pageSize, Sort.by(sort))).toList();
    }

    @Override
    public Optional<Client> getClient(@Nullable String uuidString) {
        UUID uuid = validator.validateStringUUIDAndReturnNullOrUUID(uuidString);
        if (uuid == null) return Optional.empty();
        Optional<Client> byId = clientRepository.findById(uuid);
        if (byId.isEmpty()) return Optional.empty();
        return byId.get().isRemoved() ? Optional.empty() : byId;
    }
}
