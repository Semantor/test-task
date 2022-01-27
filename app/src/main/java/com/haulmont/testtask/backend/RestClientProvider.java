package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.ClientExchanger;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.dto.ClientDTO;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class RestClientProvider {
    private final ClientRepository clientRepository;
    private final ClientExchanger clientExchanger;

    public Result<Optional<ClientDTO>> getClientById(UUID clientId) {
        try {
            Optional<Client> optionalClient = clientRepository.findById(clientId);
            if (optionalClient.isEmpty() || optionalClient.get().isRemoved())
                return Result.success(Optional.empty());
            return Result.success(Optional.of(clientExchanger.exchangeToDTO(optionalClient.get())));
        } catch (Exception exception) {
            return Result.failure(exception);
        }
    }
}
