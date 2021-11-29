package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
public class ClientSaverImpl implements ClientSaver {

    private final ClientRepository clientRepository;
    private final ClientFieldsValidator clientFieldsValidator;
    private final Validator validator;

    @Override
    public void save(Client client) {
        if (client == null ||
                client.getClientId() == null ||
                clientRepository.findById(client.getClientId()).isPresent() ||
                validator.isNullOrBlank(client.getLastName()) ||
                validator.isNullOrBlank(client.getFirstName()) ||
                !clientFieldsValidator.validatePhone(client.getPhoneNumber()) ||
                !clientFieldsValidator.validateEmail(client.getEmail()) ||
                !clientFieldsValidator.validatePassport(client.getPassport())
        ) return;
        log.info("trying to save client : " + client);
        clientRepository.save(client);
    }


    @Override
    public void save(String uuid, Client client) {
        UUID uuid1 = validator.validateUUID(uuid);
        if (uuid1 == null) return;

        Optional<Client> byId = clientRepository.findById(uuid1);
        if (byId.isEmpty()) return;
        Client persistClient = byId.get();
        if (!validator.isNullOrBlank(client.getFirstName()))
            persistClient.setFirstName(client.getFirstName());

        if (!validator.isNullOrBlank(client.getLastName()))
            persistClient.setLastName(client.getLastName());

        if (!validator.isNullOrBlank(client.getPatronymic()))
            persistClient.setPatronymic(client.getPatronymic());

        if (clientFieldsValidator.validateEmail(client.getEmail()))
            persistClient.setEmail(client.getEmail());

        if (clientFieldsValidator.validatePhone(client.getPhoneNumber()))
            persistClient.setPhoneNumber(client.getPhoneNumber());

        if (clientFieldsValidator.validatePassport(client.getPassport()))
            persistClient.setPassport(client.getPassport());

        clientRepository.save(persistClient);
    }
}
