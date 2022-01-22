package com.haulmont.testtask.view;

import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.function.ValueProvider;

import static com.haulmont.testtask.settings.ComponentSettings.*;
import static com.haulmont.testtask.settings.Labels.*;

public interface ClientGridTuner {

    default void tuneClientGrid(Grid<Client> clientGrid) {
        clientGrid.addColumn(Client::getLastName).setHeader(LAST_NAME_LABEL);
        clientGrid.addColumn(Client::getFirstName).setHeader(FIRST_NAME_LABEL);
        clientGrid.addColumn(Client::getPatronymic).setHeader(PATRONYMIC_LABEL);
        clientGrid.addColumn(Client::getPhoneNumber).setHeader(PHONE_NUMBER_LABEL);
        clientGrid.addColumn(Client::getEmail).setHeader(EMAIL_LABEL);
        clientGrid.addColumn((ValueProvider<Client, String>) client ->
                        client.getPassport().substring(PASSPORT_SERIES_START_INDEX, PASSPORT_NUMBER_START_INDEX) +
                                PASSPORT_DELIMITER +
                                client.getPassport().substring(PASSPORT_NUMBER_START_INDEX))
                .setHeader(PASSPORT_LABEL);
    }
}
