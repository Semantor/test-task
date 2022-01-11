package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.CreditOfferProviderByClient;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.haulmont.testtask.Setting.*;

@Slf4j
public class CreditOfferGridLayout extends VerticalLayout implements HasEvent, CanBeShown {
    private final CreditOfferProviderByClient creditOfferProviderByClient;

    private final Grid<CreditOffer> grid = new Grid<>();
    @Setter
    @Getter
    private Client client;

    public CreditOfferGridLayout(CreditOfferProviderByClient creditOfferProviderByClient) {
        this.creditOfferProviderByClient = creditOfferProviderByClient;
        tuneGrid();
        add(grid);
    }


    private void tuneGrid() {
        grid.setHeightFull();
        grid.setItems(Collections.emptyList());

        grid.addColumn((ValueProvider<CreditOffer, String>) creditOffer ->
                        creditOffer.getCredit().getBank().getName())
                .setHeader(BANK_NAME_LABEL);

        grid.addColumn(new NumberRenderer<>(creditOffer -> creditOffer.getCredit().getCreditLimit(), CREDIT_LIMIT_FORMAT,
                        Locale.US, CREDIT_LIMIT_DEFAULT_VALUE))
                .setHeader(CREDIT_LIMIT_LABEL);
        grid.addColumn(new NumberRenderer<>(creditOffer -> creditOffer.getCredit().getCreditRate(), CREDIT_RATE_FORMAT,
                        Locale.US, CREDIT_RATE_DEFAULT_VALUE))
                .setHeader(CREDIT_RATE_LABEL);

        grid.addColumn(new NumberRenderer<>(CreditOffer::getCreditAmount, CREDIT_LIMIT_FORMAT,
                Locale.US, CREDIT_LIMIT_DEFAULT_VALUE)).setHeader(AMOUNT_LABEL);
        grid.addColumn(CreditOffer::getMonthCount).setHeader(MONTH_LABEL);
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.addComponentColumn(creditOffer -> {
            Button button = new Button();
            button.setText(DETAILS_BUTTON_TEXT);
            button.addThemeVariants(DETAILS_STYLE);
            button.addClickListener(event -> fireEvent(new DetailsEvent(this, creditOffer.getPayments())));
            return button;
        });
        grid.addComponentColumn(creditOffer -> {
            Button delete = new Button();
            delete.setText(Setting.DELETE_BUTTON_TEXT);
            delete.addThemeVariants(Setting.DELETE_STYLE);
            delete.addClickListener(event -> fireEvent(new CreditOfferGridLayout.DeleteEvent(this, creditOffer)));
            return delete;
        });
    }

    /**
     * MUST BE {@link #setClient(Client)} FIRST
     */
    @Override
    public void show() {
        grid.setItems(creditOfferProviderByClient.getByClient(client));
    }

    public static class CreateEvent extends ComponentEvent<CreditOfferGridLayout> {
        @Getter
        private final Client client;

        public CreateEvent(CreditOfferGridLayout source, Client client) {
            super(source, false);
            this.client = client;
        }
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    public static class DetailsEvent extends ComponentEvent<CreditOfferGridLayout> {
        @Getter
        private final List<Payment> list;

        public DetailsEvent(CreditOfferGridLayout source, List<Payment> list) {
            super(source, false);
            this.list = list;
        }
    }

    public static class DeleteEvent extends ComponentEvent<CreditOfferGridLayout> {
        @Getter
        private final CreditOffer creditOffer;

        public DeleteEvent(CreditOfferGridLayout source, CreditOffer creditOffer) {
            super(source, false);
            this.creditOffer = creditOffer;
        }
    }
}
