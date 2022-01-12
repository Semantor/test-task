package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientProvider;
import com.haulmont.testtask.backend.StringUUIDHandler;
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
import java.util.stream.Collectors;

@AllArgsConstructor
public class ClientProviderImpl implements ClientProvider {

    private final ClientRepository clientRepository;
    private final StringUUIDHandler stringUUIDHandler;

    @Override
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getClients(int pageSize, int pageNumber, @NotNull String sort) {
        if (pageSize < 1 || pageNumber < 0) return Collections.emptyList();
        return clientRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sort)))
                .stream().collect(Collectors.toUnmodifiableList());

    }

    @Override
    public Optional<Client> getClient(@Nullable String uuidString) {
        UUID uuid = stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(uuidString);
        if (uuid == null) return Optional.empty();
        return clientRepository.findById(uuid);
    }
}
