package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.ClientProvider;
import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.List;


@Slf4j
public class ClientGridLayout extends VerticalLayout implements HasEvent {

    private final int DEFAULT_PAGE_SIZE;
    private final String DEFAULT_SORT_COLUMN;

    private int currentPage;
    private int currentPageSize;
    private String currentSort;


    private final ClientProvider clientProvider;
    @Getter
    private final CreditOfferGridLayout creditOfferGridLayout;
    private final Grid<Client> clientGrid = new Grid<>();

    private boolean isFinal = false;

    @PostConstruct
    private void init() {
        setDefaultPaginationOptions();
    }

    private void setDefaultPaginationOptions() {
        currentPage = 0;
        currentPageSize = DEFAULT_PAGE_SIZE;
        currentSort = DEFAULT_SORT_COLUMN;
        isFinal = false;
    }

    public ClientGridLayout(ClientProvider clientProvider, CreditOfferGridLayout creditOfferGridLayout, Integer DEFAULT_PAGE_SIZE, String DEFAULT_SORT_COLUMN) {
        this.DEFAULT_PAGE_SIZE = DEFAULT_PAGE_SIZE;
        this.DEFAULT_SORT_COLUMN = DEFAULT_SORT_COLUMN;
        this.creditOfferGridLayout = creditOfferGridLayout;
        this.clientProvider = clientProvider;
        log.info("create new client grid with " + DEFAULT_PAGE_SIZE + " page size and " + DEFAULT_SORT_COLUMN + " default sort column");
        clientGrid.setItems(clientProvider.getClients(DEFAULT_PAGE_SIZE, 0, DEFAULT_SORT_COLUMN));
        tuneGrid();
        add(clientGrid);
    }


    private void tuneGrid() {
        clientGrid.addColumn(Client::getLastName).setHeader("last name");
        clientGrid.addColumn(Client::getFirstName).setHeader("name");
        clientGrid.addColumn(Client::getPatronymic).setHeader("patronymic");
        clientGrid.addColumn(Client::getPhoneNumber).setHeader("phone");
        clientGrid.addColumn(Client::getEmail).setHeader("email");
        clientGrid.addColumn((ValueProvider<Client, String>) client -> client.getPassport().substring(0, 4) + " / " + client.getPassport().substring(4)).setHeader("passport");
        clientGrid.addComponentColumn(client -> {
            Button addNewCreditOfferButton = new Button();
            addNewCreditOfferButton.setText("add credit offer");
            addNewCreditOfferButton.setIcon(VaadinIcon.PLUS.create());
            addNewCreditOfferButton.addClickListener(event -> {
                fireEvent(new CreditOfferGridLayout.CreateEvent(creditOfferGridLayout, client));
                clientGrid.setDetailsVisible(client, false);
            });
            return addNewCreditOfferButton;
        }).setWidth("10%");

        clientGrid.addComponentColumn(client -> {
            Button editButton = new Button();
            editButton.setText(Constant.EDIT_TEXT_BUTTON);
            editButton.addThemeVariants(Constant.EDIT_STYLE);
            editButton.addClickListener(event -> fireEvent(new EditEvent(this, client)));
            return editButton;
        });
        clientGrid.addComponentColumn(client -> {
            Button delete = new Button();
            delete.setText(Constant.DELETE_TEXT_BUTTON);
            delete.addThemeVariants(Constant.DELETE_STYLE);
            delete.addClickListener(event -> fireEvent(new DeleteEvent(this, client)));
            return delete;
        });

        clientGrid.setItemDetailsRenderer(
                new ComponentRenderer<>(
                        item -> {
                            creditOfferGridLayout.setClient(item);
                            creditOfferGridLayout.show();
                            creditOfferGridLayout.setHeight("500px");
                            return creditOfferGridLayout;
                        }
                )
        );
        clientGrid.setDetailsVisibleOnClick(true);
    }


    /**
     * open first tuple of client with default filter options.
     */
    public void update() {
        setDefaultPaginationOptions();
        clientGrid.setItems(clientProvider.getClients(currentPageSize, currentPage, currentSort));
    }

    /**
     * open next tuple of client with current filter options.
     * doesnt do anything if current page is final page.
     */
    public void nextPage() {
        if (isFinal) return;
        List<Client> clients = clientProvider.getClients(currentPageSize, currentPage + 1, currentSort);
        if (clients.isEmpty()) {
            isFinal = true;
            return;
        }
        currentPage++;
        clientGrid.setItems(clients);
    }

    /**
     * open previous tuple of client with current filter options.
     * doesnt do anything if current page is first page.
     */
    public void previousPage() {
        if (currentPage == 0) return;
        clientGrid.setItems(clientProvider.getClients(currentPageSize, --currentPage, currentSort));
        isFinal = false;
    }

    public void sort(String sortColumn) {
        currentSort = sortColumn;
        clientGrid.setItems(clientProvider.getClients(currentPageSize, 0, currentSort));
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    public static class EditEvent extends ComponentEvent<ClientGridLayout> {
        @Getter
        private final Client client;

        public EditEvent(ClientGridLayout source, Client client) {
            super(source, false);
            this.client = client;
        }
    }

    public static class DeleteEvent extends ComponentEvent<ClientGridLayout> {
        @Getter
        private final Client client;

        public DeleteEvent(ClientGridLayout source, Client client) {
            super(source, false);
            this.client = client;
        }
    }
}
