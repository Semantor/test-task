package com.haulmont.testtask.api;

import com.haulmont.testtask.backend.RestNonRemovedClientsProvider;
import com.haulmont.testtask.model.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ClientsApiDelegateImpl implements ClientsApiDelegate {
    private final RestNonRemovedClientsProvider restNonRemovedClientsProvider;

    @Override
    public ResponseEntity<List<ClientDTO>> clientsGet() {
        return new ResponseEntity<>(restNonRemovedClientsProvider.getClients(), null, HttpStatus.OK);
    }
}
