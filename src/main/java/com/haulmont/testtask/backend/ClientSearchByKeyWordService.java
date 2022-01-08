package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Client;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ClientSearchByKeyWordService {

    List<Client> search(@NotNull String keyword, int pageSize, int pageNumber, @NotNull String sort);
}
