package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.model.repositories.ClientSpecifications;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.haulmont.testtask.settings.ErrorMessages.WRONG_INCOME_DATA;

@RequiredArgsConstructor
@Component
public class ClientSearchByKeyWordService {
    private final ClientRepository clientRepository;
    private final ClientSpecifications clientSpecifications;

    public Result<List<Client>> search(@NotNull String keyword, int pageSize, int pageNumber, @NotNull String sort) {
        try {
            if (pageSize < 1 || pageNumber < 0)
                throw new IllegalArgumentExceptionWithoutStackTrace(WRONG_INCOME_DATA);

            return Result.success(clientRepository.findAll(clientSpecifications.searchNonRemovedClientByAllField(keyword),
                    PageRequest.of(pageNumber, pageSize, Sort.by(sort))).toList());
        } catch (Exception ex) {
            return Result.failure(ex);
        }
    }
}
