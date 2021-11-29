package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.*;
import com.haulmont.testtask.backend.excs.DeleteException;
import com.haulmont.testtask.model.entity.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeleteForm extends VerticalLayout implements CanBeShown, CanBeClosed, HasEvent {
    private final Button close = new Button();
    @Getter
    private final Button accept = new Button();
    private final H2 h2label = new H2();
    @Getter
    private final Div objectDiv = new Div();
    private Remover remover;
    private final BankRemover bankRemover;
    private final CreditRemover creditRemover;
    private final CreditOfferRemover creditOfferRemover;
    private final ClientRemover clientRemover;

    private Removable removable;

    public DeleteForm(BankRemover bankRemover, CreditRemover creditRemover, CreditOfferRemover creditOfferRemover, ClientRemover clientRemover) {
        this.bankRemover = bankRemover;
        this.clientRemover = clientRemover;
        this.creditOfferRemover = creditOfferRemover;
        this.creditRemover = creditRemover;
        h2label.setText(Constant.DELETE_TEXT);
        add(h2label, objectDiv, createButtons());
    }

    private HorizontalLayout createButtons() {
        accept.setText(Constant.ACCEPT_TEXT_BUTTON);
        accept.addThemeVariants(Constant.SAVE_STYLE);
        accept.addClickShortcut(Key.ENTER);
        accept.addClickListener(event -> accept());
        tuneCloseButton();
        return new HorizontalLayout(accept, close);
    }

    @Override
    public Button getCloseButton() {
        return close;
    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    private void accept() {
        try {
            remover.remove(removable);
            log.info("successful remove client:" + removable);
            Notification.show("successful remove", Constant.NOTIFICATION_DURATION, Constant.DEFAULT_POSITION);
            close();
            fireEvent(new UpdateEvent(this));
        } catch (DeleteException ex) {
            log.warn(ex.getMessage());
            Notification.show(ex.getMessage(), Constant.NOTIFICATION_DURATION, Constant.DEFAULT_POSITION);
        }
    }

    public void setClient(Client client) {
        this.remover = clientRemover;
        this.removable = client;
    }

    public void setBank(Bank bank) {
        this.remover = bankRemover;
        this.removable = bank;
    }

    public void setCredit(Credit credit) {
        this.remover = creditRemover;
        this.removable = credit;
    }

    public void setCreditOffer(CreditOffer creditOffer) {
        this.remover = creditOfferRemover;
        this.removable = creditOffer;
    }

    /**
     * USE one of the setter first
     * {@link #setClient(Client)}
     * {@link #setBank(Bank)}
     * {@link #setCredit(Credit)}
     * {@link #setCreditOffer(CreditOffer)}
     */
    @Override
    public void show() {
        objectDiv.setText(removable.toDeleteString());
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    public static class CloseEvent extends ComponentEvent<DeleteForm> {
        public CloseEvent(DeleteForm source) {
            super(source, false);
        }
    }

    public static class UpdateEvent extends ComponentEvent<DeleteForm>{
        public UpdateEvent(DeleteForm source) {
            super(source, false);
        }
    }
}
