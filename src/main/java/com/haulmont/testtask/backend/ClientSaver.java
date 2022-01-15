package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.excs.Result;
import com.haulmont.testtask.backend.util.ConstraintViolationHandler;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.Optional;
import java.util.Set;
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
    private final ClientRepository clientRepository;
    private final StringUUIDHandler stringUUIDHandler;
    private final javax.validation.Validator validator;

    /**
     * check that UUID is unused in THIS table
     * and check fields
     * {@link Client#getFirstName()}
     * {@link Client#getLastName()}
     * {@link Client#getEmail()}
     * {@link Client#getPhoneNumber()}
     * {@link Client#getPassport()}
     * for not null and valid with
     * Patronymic can be null or Empty.
     * firstname, lastname or patronymic can not contain non-letter symbols
     */
    public Result<Boolean> save(Client client) throws CreateClientException {
        Set<ConstraintViolation<Client>> constraintViolations = validator.validate(client);
        if (!constraintViolations.isEmpty())
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(
                    ConstraintViolationHandler.handleToString(constraintViolations)));

        if (clientRepository.findById(client.getClientId()).isPresent()) {
            throw new CreateClientException(UUID_IS_ALREADY_USED);
        }

        log.info(LOG_TEMPLATE_3, TRYING_TO_SAVE_NEW_CLIENT, LOG_DELIMITER, client);
        try {
            clientRepository.save(client);
        } catch (Exception ex) {
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(ex.getMessage()));
        }
        return Result.success(true);
    }

    /**
     * update only not null fields exclude UUID
     * This method can not to change fields to null value.
     */
    public Result<Boolean> save(String uuidString, Client client) {
        UUID uuid = stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(uuidString);
        if (uuid == null)
            return Result.failure(new CreateClientException(NULLABLE_ID));

        Optional<Client> byId = clientRepository.findById(uuid);
        if (byId.isEmpty()) return Result.failure(new CreateClientException(CLIENT_DOES_NOT_PRESENT_IN_DB));
        Client persistClient = byId.get();
        if (validator.validateValue(Client.class, CLIENT_FIRST_NAME_FOR_SORTING, client.getFirstName()).isEmpty())
            persistClient.setFirstName(client.getFirstName());

        if (validator.validateValue(Client.class, CLIENT_LAST_NAME_FOR_SORTING, client.getLastName()).isEmpty())
            persistClient.setLastName(client.getLastName());

        if (validator.validateValue(Client.class, CLIENT_PATRONYMIC_FOR_SORTING, client.getPatronymic()).isEmpty())
            persistClient.setPatronymic(client.getPatronymic());

        if (validator.validateValue(Client.class, CLIENT_EMAIL_FOR_SORTING, client.getEmail()).isEmpty())
            persistClient.setEmail(client.getEmail());

        if (validator.validateValue(Client.class, CLIENT_PHONE_NUMBER_FOR_SORTING, client.getPhoneNumber()).isEmpty())
            persistClient.setPhoneNumber(client.getPhoneNumber());

        if (validator.validateValue(Client.class, CLIENT_PASSPORT_FOR_SORTING, client.getPassport()).isEmpty())
            persistClient.setPassport(client.getPassport());

        try {
            clientRepository.save(persistClient);
        } catch (Exception ex) {
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(ex.getMessage()));
        }
        return Result.success(true);
    }
}
