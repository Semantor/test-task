package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.ClientProvider;
import com.haulmont.testtask.backend.ClientSearchByKeyWordService;
import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

import static com.haulmont.testtask.settings.ButtonSettings.*;
import static com.haulmont.testtask.settings.ComponentSettings.ADD_NEW_CREDIT_OFFER_BUTTON_WIDTH;
import static com.haulmont.testtask.settings.ComponentSettings.CREDIT_OFFER_GRID_LAYOUT_HEIGHT;


@Slf4j
public class ClientGridLayout extends VerticalLayout implements HasEvent, ClientGridTuner, Hornable {

    private final int DEFAULT_PAGE_SIZE;
    private final String DEFAULT_SORT_COLUMN;

    private int currentPage;
    private int currentPageSize;
    private String currentSort;


    private final transient ClientProvider clientProvider;
    private final transient ClientSearchByKeyWordService searchByKeyWordService;
    @Getter
    private final CreditOfferGridLayout creditOfferGridLayout;
    private final Grid<Client> clientGrid = new Grid<>();

    private boolean isFinal = false;
    private boolean isSearch = false;
    private String searchKeyword;

    @PostConstruct
    private void init() {
        update();
    }

    private void setDefaultPaginationOptions() {
        currentPage = 0;
        currentPageSize = DEFAULT_PAGE_SIZE;
        currentSort = DEFAULT_SORT_COLUMN;
        isFinal = false;
        isSearch = false;
    }

    public ClientGridLayout(ClientProvider clientProvider, CreditOfferGridLayout creditOfferGridLayout,
                            Integer defaultPageSize, String defaultSortColumn,
                            ClientSearchByKeyWordService searchByKeyWordService) {
        this.DEFAULT_PAGE_SIZE = defaultPageSize;
        this.DEFAULT_SORT_COLUMN = defaultSortColumn;
        this.creditOfferGridLayout = creditOfferGridLayout;
        this.clientProvider = clientProvider;
        this.searchByKeyWordService = searchByKeyWordService;
        tuneGrid();
        add(clientGrid);
    }


    private void tuneGrid() {
        tuneClientGrid(clientGrid);
        clientGrid.addComponentColumn(client -> {
            Button addNewCreditOfferButton = new Button();
            addNewCreditOfferButton.setText(ADD_CREDIT_OFFER_BUTTON_TEXT);
            addNewCreditOfferButton.setIcon(VaadinIcon.PLUS.create());
            addNewCreditOfferButton.addClickListener(event -> {
                fireEvent(new CreditOfferGridLayout.CreateEvent(creditOfferGridLayout, client));
                clientGrid.setDetailsVisible(client, false);
            });
            return addNewCreditOfferButton;
        }).setWidth(ADD_NEW_CREDIT_OFFER_BUTTON_WIDTH);

        clientGrid.addComponentColumn(client -> {
            Button editButton = new Button();
            editButton.setText(EDIT_BUTTON_TEXT);
            editButton.addThemeVariants(EDIT_STYLE);
            editButton.addClickListener(event -> fireEvent(new EditEvent(this, client)));
            return editButton;
        });
        clientGrid.addComponentColumn(client -> {
            Button delete = new Button();
            delete.setText(DELETE_BUTTON_TEXT);
            delete.addThemeVariants(DELETE_STYLE);
            delete.addClickListener(event -> fireEvent(new DeleteEvent(this, client)));
            return delete;
        });

        clientGrid.setItemDetailsRenderer(new ComponentRenderer<>(item -> {
            creditOfferGridLayout.setClient(item);
            creditOfferGridLayout.show();
            creditOfferGridLayout.setHeight(CREDIT_OFFER_GRID_LAYOUT_HEIGHT);
            return creditOfferGridLayout;
        }));
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
     * doesn't do anything if current page is final page.
     */
    public void nextPage() {
        if (isFinal) return;
        List<Client> clients;
        if (!isSearch) clients = clientProvider
                .getClients(currentPageSize, currentPage + 1, currentSort);
        else
            clients = searchByKeyWordService
                    .search(searchKeyword, currentPageSize, currentPage + 1, currentSort)
                    .fold(clients1 -> clients1, exception -> {
                        hornIntoNotificationAndLoggerInfo(exception.getMessage());
                        return Collections.emptyList();
                    });
        if (clients.isEmpty()) {
            isFinal = true;
            return;
        }
        currentPage++;
        clientGrid.setItems(clients);
    }

    /**
     * open previous tuple of client with current filter options.
     * doesn't do anything if current page is first page.
     */
    public void previousPage() {
        if (currentPage == 0) return;
        List<Client> clients;
        if (isSearch)
            clients = searchByKeyWordService
                    .search(searchKeyword, currentPageSize, --currentPage, currentSort)
                    .fold(clients1 -> clients1, exception -> {
                        hornIntoNotificationAndLoggerInfo(exception.getMessage());
                        return Collections.emptyList();
                    });
        else clients = clientProvider.getClients(currentPageSize, --currentPage, currentSort);

        clientGrid.setItems(clients);
        isFinal = false;
    }

    public void search(String keyword) {
        setDefaultPaginationOptions();
        searchKeyword = keyword;
        isSearch = true;
        clientGrid.setItems((List<Client>) searchByKeyWordService
                .search(searchKeyword, currentPageSize, currentPage, currentSort)
                .fold(clients -> clients, exception -> {
                    hornIntoNotificationAndLoggerInfo(exception.getMessage());
                    return Collections.emptyList();
                }));
    }

    public void sort(String sortColumn) {
        currentSort = sortColumn;
        clientGrid.setItems(clientProvider.getClients(currentPageSize, 0, currentSort));
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    @Override
    public Logger log() {
        return log;
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
