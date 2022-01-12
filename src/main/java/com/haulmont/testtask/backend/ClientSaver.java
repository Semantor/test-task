package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.haulmont.testtask.Setting.*;
import static com.haulmont.testtask.view.Hornable.LOG_TEMPLATE_3;

/**
 * Validating incoming client and trying to save.
 * {@link #save(String, Client)} is update method.
 */
@AllArgsConstructor
@Slf4j
@Component
public class ClientSaver {
    private final ClientFieldsValidator clientFieldsValidator;
    private final ClientRepository clientRepository;
    private final Validator validator;
    private final StringUUIDHandler stringUUIDHandler;

    /**
     * check that UUID is unused in THIS table
     * and check fields
     * {@link Client#getFirstName()}
     * {@link Client#getLastName()}
     * {@link Client#getEmail()}
     * {@link Client#getPhoneNumber()}
     * {@link Client#getPassport()}
     * for not null and valid with {@link ClientFieldsValidator}
     * Patronymic can be null or Empty.
     * firstname, lastname or patronymic can not contain non-letter symbols
     *
     * @throws com.haulmont.testtask.backend.excs.CreateClientException if isn't valid
     */
    public void save(@Nullable Client client) throws CreateClientException {
        if (client == null) throw new CreateClientException(NULLABLE_CLIENT);
        if (client.getClientId() == null) throw new CreateClientException(NULLABLE_ID);
        if (clientRepository.findById(client.getClientId()).isPresent()) {
            throw new CreateClientException(UUID_IS_ALREADY_USED);
        }

        if (validator.isNullOrBlank(client.getLastName()))
            throw new CreateClientException(EMPTY_LASTNAME);
        if (client.getLastName().length() < NAME_FIELD_MIN_LENGTH)
            throw new CreateClientException(TOO_SHORT_LASTNAME);
        if (!client.getLastName().matches(ONLY_LETTER_REG_EX))
            throw new CreateClientException(LASTNAME_INCORRECT_SYMBOLS);

        if (validator.isNullOrBlank(client.getFirstName()))
            throw new CreateClientException(EMPTY_NAME);
        if (client.getFirstName().length() < NAME_FIELD_MIN_LENGTH)
            throw new CreateClientException(TOO_SHORT_NAME);
        if (!client.getFirstName().matches(ONLY_LETTER_REG_EX))
            throw new CreateClientException(NAME_INCORRECT_SYMBOLS);

        if (!clientFieldsValidator.validatePhone(client.getPhoneNumber()))
            throw new CreateClientException(NO_VALID_PHONE);
        if (!clientFieldsValidator.validateEmail(client.getEmail()))
            throw new CreateClientException(NO_VALID_EMAIL);
        if (!clientFieldsValidator.validatePassport(client.getPassport()))
            throw new CreateClientException(NO_VALID_PASSPORT);

        log.info(LOG_TEMPLATE_3, TRYING_TO_SAVE_NEW_CLIENT, LOG_DELIMITER, client);
        try {
            clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new CreateClientException(ex.getMessage());
        }
    }

    /**
     * update only not null fields exclude UUID
     * use {@link ClientFieldsValidator#validatePhone(String)}, {@link ClientFieldsValidator#validateEmail(String)},
     * {@link ClientFieldsValidator#validatePassport(String)} to validate
     * This method can not to change fields to null value.
     */
    public void save(String uuidString, Client client) {
        UUID uuid = stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(uuidString);
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
