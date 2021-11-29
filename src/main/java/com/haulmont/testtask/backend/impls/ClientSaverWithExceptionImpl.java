package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
public class ClientSaverWithExceptionImpl implements ClientSaver {
    private final ClientFieldsValidator clientFieldsValidator;
    private final ClientRepository clientRepository;
    private final Validator validator;

    @Override
    public void save(Client client) throws CreateClientException {
        if (client==null) throw new CreateClientException("nullabe client");
        if (client.getClientId() == null) throw new CreateClientException(CreateClientException.EMPTY_UUID);
        if (clientRepository.findById(client.getClientId()).isPresent())
            throw new CreateClientException(CreateClientException.UUID_IS_ALREADY_USED);
        if (validator.isNullOrBlank(client.getLastName()))
            throw new CreateClientException(CreateClientException.EMPTY_LASTNAME);
        if (validator.isNullOrBlank(client.getFirstName()))
            throw new CreateClientException(CreateClientException.EMPTY_NAME);
        if (!clientFieldsValidator.validatePhone(client.getPhoneNumber()))
            throw new CreateClientException(CreateClientException.NO_VALID_PHONE);
        if (!clientFieldsValidator.validateEmail(client.getEmail()))
            throw new CreateClientException(CreateClientException.NO_VALID_EMAIL);
        if (!clientFieldsValidator.validatePassport(client.getPassport()))
            throw new CreateClientException(CreateClientException.NO_VALID_PASSPORT);
        log.info("trying to save client : " + client);
        try {
            clientRepository.save(client);
        } catch (DataIntegrityViolationException ex){
            throw new CreateClientException(ex.getMessage());
        }
    }

    @Override
    public void save(String uuid, Client client) {
        UUID uuid1 = validator.validateUUID(uuid);
        if (uuid1 == null) throw new CreateClientException(CreateClientException.WRONG_UUID);

        Optional<Client> byId = clientRepository.findById(uuid1);
        if (byId.isEmpty()) throw new CreateClientException(CreateClientException.DOES_NOT_PRESENT);
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
