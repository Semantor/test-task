package com.haulmont.testtask.api;

import com.haulmont.testtask.backend.RestClientCreator;
import com.haulmont.testtask.backend.RestClientProvider;
import com.haulmont.testtask.backend.RestNonRemovedClientsProvider;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ClientsApiDelegateImpl implements ClientsApiDelegate {
    private final RestNonRemovedClientsProvider restNonRemovedClientsProvider;
    private final RestClientProvider restClientProvider;
    private final RestClientCreator restClientCreator;

    @Override

    public ResponseEntity<List<ClientDTO>> getAllClient() {
        List<ClientDTO> resultClients = restNonRemovedClientsProvider.getClients();
        if (resultClients.isEmpty())
            return new ResponseEntity<>(Collections.emptyList(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(resultClients, null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ClientDTO> getClientById(UUID clientId) {
        Result<Optional<ClientDTO>> clientByIdResult = restClientProvider.getClientById(clientId);
        if (clientByIdResult.isFailure())
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        if (clientByIdResult.getOrNull().isPresent())
            return new ResponseEntity<>(clientByIdResult.getOrNull().get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> createNewClient(ClientDTO clientDTO) {
        Result<Optional<String>> resultOfOptionalConstraintViolation =
                restClientCreator.createNewClientAndReturnResultOfViolationConstraint(clientDTO);
        if (resultOfOptionalConstraintViolation.isFailure())
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        if (resultOfOptionalConstraintViolation.getOrNull().isEmpty())
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(resultOfOptionalConstraintViolation.getOrNull().get(), HttpStatus.BAD_REQUEST);
    }
}
