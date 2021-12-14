package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.CreditOfferProviderByClient;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.haulmont.testtask.view.Constant.creditLimitFormat;
import static com.haulmont.testtask.view.Constant.creditRateFormat;

@Slf4j
public class CreditOfferGridLayout extends VerticalLayout implements HasEvent, CanBeShown {
    private final CreditOfferProviderByClient creditOfferProviderByClient;

    private final Grid<CreditOffer> grid = new Grid<>();
    private final Button createNewCreditOfferButton = new Button("create new credit offer");
    @Setter
    @Getter
    private Client client;

    public CreditOfferGridLayout(CreditOfferProviderByClient creditOfferProviderByClient) {
        this.creditOfferProviderByClient = creditOfferProviderByClient;
        tuneGrid();
        add(tuneTopButtons(), grid);
    }

    private HorizontalLayout tuneTopButtons() {
        createNewCreditOfferButton.setIcon(new Icon(VaadinIcon.PLUS));
        createNewCreditOfferButton.addClickListener(event ->
                fireEvent(new CreditOfferGridLayout.CreateEvent(this, client)));
        return new HorizontalLayout(createNewCreditOfferButton);
    }

    private void tuneGrid() {
        grid.setHeightFull();
        grid.setItems(Collections.emptyList());

        grid.addColumn((ValueProvider<CreditOffer, UUID>) creditOffer ->
                        creditOffer.getCredit().getBank().getBankId())
                .setHeader("bank uuid");

        grid.addColumn(new NumberRenderer<>(creditOffer -> creditOffer.getCredit().getCreditLimit(), creditLimitFormat,
                        Locale.US, "$ 0.00"))
                .setHeader("credit limit");
        grid.addColumn(new NumberRenderer<>(creditOffer -> creditOffer.getCredit().getCreditRate(), creditRateFormat,
                        Locale.US, "$ 0.00"))
                .setHeader("credit rate");

        grid.addColumn(new NumberRenderer<>(CreditOffer::getCreditAmount, creditLimitFormat,
                Locale.US, "$ 0.00")).setHeader("amount");
        grid.addColumn(CreditOffer::getMonthCount).setHeader("month count");
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.addComponentColumn(creditOffer -> {
            Button button = new Button();
            button.setText("Details");
            button.addThemeVariants(ButtonVariant.LUMO_SMALL);
            button.addClickListener(event -> fireEvent(new DetailsEvent(this, creditOffer.getPayments())));
            return button;
        });
        grid.addComponentColumn(creditOffer -> {
            Button delete = new Button();
            delete.setText(Constant.DELETE_TEXT_BUTTON);
            delete.addThemeVariants(Constant.DELETE_STYLE);
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
