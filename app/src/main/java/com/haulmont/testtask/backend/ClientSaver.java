package com.haulmont.testtask.backend;

import com.haulmont.testtask.backend.util.ConstraintViolationHandler;
import com.haulmont.testtask.backend.util.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.backend.util.Result;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.haulmont.testtask.settings.ErrorMessages.CLIENT_DOES_NOT_PRESENT_IN_DB;
import static com.haulmont.testtask.settings.ErrorMessages.UUID_IS_ALREADY_USED;
import static com.haulmont.testtask.settings.FieldNameSettings.*;
import static com.haulmont.testtask.settings.Responses.LOG_DELIMITER;
import static com.haulmont.testtask.settings.Responses.TRYING_TO_SAVE_NEW_CLIENT;
import static com.haulmont.testtask.view.Hornable.LOG_TEMPLATE_3;

/**
 * Validating incoming client and trying to save.
 * {@link #save(UUID, Client)} is update method.
 */
@AllArgsConstructor
@Slf4j
@Component
public class ClientSaver {
    private final ClientRepository clientRepository;
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
    public Result<Boolean> save(Client client) {
        Set<ConstraintViolation<Client>> constraintViolations = validator.validate(client);
        if (!constraintViolations.isEmpty())
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(
                    ConstraintViolationHandler.handleToString(constraintViolations)));

        if (clientRepository.findById(client.getClientId()).isPresent())
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(UUID_IS_ALREADY_USED));


        log.info(LOG_TEMPLATE_3, TRYING_TO_SAVE_NEW_CLIENT, LOG_DELIMITER, client);
        try {
            clientRepository.save(client);
        } catch (Exception ex) {
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(ex.getMessage()));
        }
        return Result.success(true);
    }

    /**
     * update only not null fields :
     * firs name
     * last name
     * patronymic
     * This method can not to change fields to null value.
     */
    public Result<Boolean> save(UUID updatingUserUUID, Client client) {
        try {
            Optional<Client> optionalPersistClient = clientRepository.findById(updatingUserUUID);

            if (optionalPersistClient.isEmpty())
                throw new IllegalArgumentExceptionWithoutStackTrace(CLIENT_DOES_NOT_PRESENT_IN_DB);
            Client persistClient = optionalPersistClient.get();
            if (validator.validateValue(Client.class, CLIENT_FIRST_NAME_FIELD_NAME, client.getFirstName()).isEmpty())
                persistClient.setFirstName(client.getFirstName());

            if (validator.validateValue(Client.class, CLIENT_LAST_NAME_FIELD_NAME, client.getLastName()).isEmpty())
                persistClient.setLastName(client.getLastName());

            if (validator.validateValue(Client.class, CLIENT_PATRONYMIC_FIELD_NAME, client.getPatronymic()).isEmpty()) {
                persistClient.setPatronymic(client.getPatronymic());
            }

            clientRepository.save(persistClient);
        } catch (Exception ex) {
            return Result.failure(new IllegalArgumentExceptionWithoutStackTrace(ex.getMessage()));
        }
        return Result.success(true);
    }
}
