package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class ClientProvider {
    private final ClientRepository clientRepository;
    private final StringUUIDHandler stringUUIDHandler;

    /**
     * return all available clients (non-removed)
     */
    public List<Client> getClients() {
        return clientRepository.findByIsRemoved(false);
    }


    /**
     * trying to return list of client using pagination
     * sorting available on fields :
     * firstName
     * lastName
     * patronymic
     * email
     * phoneNumber
     * passport
     * <p>
     * all fields sorting like string.
     *
     * @param pageSize   number of client
     * @param pageNumber number of page
     * @param sort       sorting field
     */
    public List<Client> getClients(int pageSize, int pageNumber, @NotNull String sort) {
        if (pageSize < 1 || pageNumber < 0) return Collections.emptyList();
        return clientRepository.findByIsRemoved(false, PageRequest.of(pageNumber, pageSize, Sort.by(sort))).toList();
    }

    /**
     * trying to return non-removed client by client id
     * validate uuid
     */
    public Optional<Client> getClient(@Nullable String uuidString) {
        UUID uuid = stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(uuidString);
        if (uuid == null) return Optional.empty();
        Optional<Client> byId = clientRepository.findById(uuid);
        if (byId.isEmpty()) return Optional.empty();
        return byId.get().isRemoved() ? Optional.empty() : byId;
    }
}
