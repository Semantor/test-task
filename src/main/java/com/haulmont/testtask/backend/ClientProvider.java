package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface ClientProvider {
    /**
     * return all available clients (non-removed)
     */
    List<Client> getClients();

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
    List<Client> getClients(int pageSize, int pageNumber, @NotNull String sort);

    /**
     * trying to return client by client id
     * validate uuid
     */
    Optional<Client> getClient(@Nullable String uuid);
}
