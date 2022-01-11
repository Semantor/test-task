package com.haulmont.testtask.backend.impls;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import com.haulmont.testtask.view.Hornable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static com.haulmont.testtask.Setting.*;
import static com.haulmont.testtask.view.Hornable.LOG_TEMPLATE_3;

@AllArgsConstructor
@Slf4j
public class ClientSaverWithExceptionImpl implements ClientSaver {
    private final ClientFieldsValidator clientFieldsValidator;
    private final ClientRepository clientRepository;
    private final Validator validator;
    private static final String ONLY_LETTER_REG_EX = "[a-zA-Z]+";
    public static final int MIN_FIRST_OR_LAST_NAME_LENGTH = 3;

    @Override
    public void save(Client client) throws CreateClientException {
        if (client == null) throw new CreateClientException(NULLABLE_CLIENT);
        if (client.getClientId() == null) throw new CreateClientException(NULLABLE_ID);
        if (clientRepository.findById(client.getClientId()).isPresent())
            throw new CreateClientException(UUID_IS_ALREADY_USED);

        if (validator.isNullOrBlank(client.getLastName()))
            throw new CreateClientException(EMPTY_LASTNAME);
        if (client.getLastName().length() < MIN_FIRST_OR_LAST_NAME_LENGTH)
            throw new CreateClientException(TOO_SHORT_LASTNAME);
        if (!client.getLastName().matches(ONLY_LETTER_REG_EX))
            throw new CreateClientException(LASTNAME_INCORRECT_SYMBOLS);

        if (validator.isNullOrBlank(client.getFirstName()))
            throw new CreateClientException(EMPTY_NAME);
        if (client.getFirstName().length() < MIN_FIRST_OR_LAST_NAME_LENGTH)
            throw new CreateClientException(TOO_SHORT_NAME);
        if (!client.getFirstName().matches(ONLY_LETTER_REG_EX))
            throw new CreateClientException(NAME_INCORRECT_SYMBOLS);

        if (!clientFieldsValidator.validatePhone(client.getPhoneNumber()))
            throw new CreateClientException(NO_VALID_PHONE);
        if (!clientFieldsValidator.validateEmail(client.getEmail()))
            throw new CreateClientException(NO_VALID_EMAIL);
        if (!clientFieldsValidator.validatePassport(client.getPassport()))
            throw new CreateClientException(NO_VALID_PASSPORT);

        log.info(LOG_TEMPLATE_3,TRYING_TO_SAVE_NEW_CLIENT,LOG_DELIMITER,client);
        try {
            clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new CreateClientException(ex.getMessage());
        }
    }

    @Override
    public void save(String uuidString, Client client) {
        UUID uuid = validator.validateStringUUIDAndReturnNullOrUUID(uuidString);
        if (uuid == null) throw new CreateClientException(NULLABLE_ID);

        Optional<Client> byId = clientRepository.findById(uuid);
        if (byId.isEmpty()) throw new CreateClientException(CLIENT_DOES_NOT_PRESENT_IN_DB);
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
