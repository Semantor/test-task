package com.haulmont.testtask.backend;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Component
public class ClientProvider {
    private final ClientRepository clientRepository;

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
        try {
            if (pageSize < 1 || pageNumber < 0)
                throw new IllegalArgumentExceptionWithoutStackTrace(Setting.WRONG_INCOME_DATA);
            return clientRepository.findByIsRemoved(false, PageRequest.of(pageNumber, pageSize, Sort.by(sort))).toList();
        } catch (Exception exception) {
            return Collections.emptyList();
        }
    }
}
