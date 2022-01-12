package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.model.repositories.ClientSpecifications;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ClientSearchByKeyWordService {
    private final ClientRepository clientRepository;
    private final ClientSpecifications clientSpecifications;

    public List<Client> search(@NotNull String keyword, int pageSize, int pageNumber, @NotNull String sort) {
        if (pageSize < 1 || pageNumber < 0) return Collections.emptyList();

        return clientRepository.findAll(clientSpecifications.searchNonRemovedClientByAllField(keyword),
                PageRequest.of(pageNumber, pageSize, Sort.by(sort))).toList();
    }
}