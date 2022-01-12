package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.backend.StringUUIDHandler;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.view.Hornable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import static com.haulmont.testtask.Setting.LOG_DELIMITER;
import static com.haulmont.testtask.Setting.TRYING_TO_SAVE_NEW_CLIENT;

@AllArgsConstructor
@Slf4j
public class ClientSaverImpl implements ClientSaver {

    private final ClientRepository clientRepository;
    private final ClientFieldsValidator clientFieldsValidator;
    private final Validator validator;
    private StringUUIDHandler stringUUIDHandler;

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
        log.info(Hornable.LOG_TEMPLATE_3, TRYING_TO_SAVE_NEW_CLIENT, LOG_DELIMITER, client);
        clientRepository.save(client);
    }


    @Override
    public void save(String uuidString, Client client) {
        UUID uuid1 = stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(uuidString);
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
