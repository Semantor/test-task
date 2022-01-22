package com.haulmont.testtask.view;

import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class BankClientsGridLayout extends VerticalLayout implements CanBeShown, ClientGridTuner {


    private final Grid<Client> clientGrid = new Grid<>();
    @Getter
    @Setter
    private Set<Client> clients = new HashSet<>();



    public BankClientsGridLayout() {
        tuneGrid();
        add(clientGrid);
    }


    private void tuneGrid() {
        tuneClientGrid(clientGrid);
    }

    /**
     * must be use {@link #setClients(Set)} first
     */
    @Override
    public void show() {
        clientGrid.setItems(clients);
    }
}
