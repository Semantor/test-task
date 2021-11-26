package com.haulmont.testtask.view;

import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class BankClientsGridLayout extends VerticalLayout implements CanBeShown {


    private final Grid<Client> clientGrid = new Grid<>();
    @Getter
    @Setter
    private Set<Client> clients = new HashSet<>();



    public BankClientsGridLayout() {
        tuneGrid();
        add(clientGrid);
    }


    private void tuneGrid() {
        clientGrid.addColumn(Client::getLastName).setHeader("last name");
        clientGrid.addColumn(Client::getFirstName).setHeader("name");
        clientGrid.addColumn(Client::getPatronymic).setHeader("patronymic");
        clientGrid.addColumn(Client::getPhoneNumber).setHeader("phone");
        clientGrid.addColumn(Client::getEmail).setHeader("email");
        clientGrid.addColumn(Client::getPassport).setHeader("passport");
    }

    /**
     * must be use {@link #setClients(Set)} first
     */
    @Override
    public void show() {
        clientGrid.setItems(clients);
    }
}
