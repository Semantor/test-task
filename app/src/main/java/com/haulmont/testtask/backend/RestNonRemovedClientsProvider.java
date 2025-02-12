package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.ClientExchanger;
import com.haulmont.testtask.model.dto.ClientDTO;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.haulmont.testtask.settings.ErrorMessages.SOME_DB_PROBLEM;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestNonRemovedClientsProvider {
    private final ClientRepository clientRepository;
    private final ClientExchanger clientExchanger;

    public List<ClientDTO> getClients() {
        List<ClientDTO> resultList = new ArrayList<>();
        try {
            for (Client client : clientRepository.findByIsRemoved(false))
                resultList.add(clientExchanger.exchangeToDTO(client));
        } catch (Exception exception) {
            log.warn(SOME_DB_PROBLEM);
            return Collections.emptyList();
        }
        return resultList;
    }
}
