package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.BankProvider;
import com.haulmont.testtask.model.entity.Bank;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;

public class BankGridLayout extends VerticalLayout implements CanBeShown, CanBeUpdated, HasEvent, CanBeClosed {

    private final BankProvider bankProvider;
    private final Grid<Bank> grid = new Grid<>();
    private final BankClientsGridLayout bankClientsGridLayout;

    private final Button close = new Button();

    private HorizontalLayout createButtons() {
        tuneCloseButton();
        return new HorizontalLayout(close);
    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    public BankGridLayout(BankProvider bankProvider, BankClientsGridLayout bankClientsGridLayout) {
        this.bankClientsGridLayout = bankClientsGridLayout;
        this.bankProvider = bankProvider;
        tuneGrid();
        add(grid, createButtons());
    }

    private void tuneGrid() {
        grid.addColumn(Bank::getBankId).setHeader("UUID");
        grid.addColumn(Bank::getName).setHeader("name");
        grid.setDetailsVisibleOnClick(true);
        grid.addComponentColumn(bank -> {
            Button delete = new Button();
            delete.setText(Constant.DELETE_TEXT_BUTTON);
            delete.addThemeVariants(Constant.DELETE_STYLE);
            delete.addClickListener(event -> fireEvent(new BankGridLayout.DeleteEvent(this, bank)));
            return delete;
        });
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(
                        item -> {
                            bankProvider.updateClients(item);
                            bankClientsGridLayout.setClients(item.getClients());
                            bankClientsGridLayout.show();
                            return bankClientsGridLayout;
                        }
                )
        );

    }

    @Override
    public void show() {
        grid.setItems(bankProvider.getAllBanks());
    }

    @Override
    public void update() {
        grid.setItems(bankProvider.getAllBanks());
    }


    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    public static class CloseEvent extends ComponentEvent<BankGridLayout> {
        public CloseEvent(BankGridLayout source) {
            super(source, false);
        }
    }

    @Override
    public Button getCloseButton() {
        return close;
    }

    public static class DeleteEvent extends ComponentEvent<BankGridLayout> {
        @Getter
        private final Bank bank;

        public DeleteEvent(BankGridLayout source, Bank bank) {
            super(source, false);
            this.bank = bank;
        }
    }
}
