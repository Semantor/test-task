package com.haulmont.testtask.backend;

import com.haulmont.testtask.model.entity.Client;
import org.jetbrains.annotations.Nullable;

/**
 * Validating incoming client and trying to save.
 * {@link #save(String, Client)} is update method.
 */
public interface ClientSaver {

    /**
     * check that UUID is unused in THIS table
     * and check fields
     * {@link Client#getFirstName()}
     * {@link Client#getLastName()}
     * {@link Client#getEmail()}
     * {@link Client#getPhoneNumber()}
     * {@link Client#getPassport()}
     * for not null and valid with {@link ClientValidator}
     * Patronymic can be null or Empty.
     * firstname, lastname or patronymic can not contain non-letter symbols
     * @throws com.haulmont.testtask.backend.excs.CreateClientException if isn't valid
     */
    void save(@Nullable Client client);

    /**
     * update only not null fields exclude UUID
     * use {@link ClientValidator#validatePhone(String)}, {@link ClientValidator#validateEmail(String)},
     * {@link ClientValidator#validatePassport(String)} to validate
     * This method can not to change fields to null value.
     */
    void save(String uuid, Client client);
}
