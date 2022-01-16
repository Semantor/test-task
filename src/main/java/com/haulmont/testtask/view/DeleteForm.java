package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.*;
import com.haulmont.testtask.backend.excs.DeleteException;
import com.haulmont.testtask.model.entity.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import static com.haulmont.testtask.Setting.SUCCESSFULLY_DELETED_USER_MESSAGE;

@Slf4j
public class DeleteForm extends VerticalLayout implements CanBeShown, CanBeClosed, HasEvent, Hornable {
    private final Button close = new Button();
    @Getter
    private final Button accept = new Button();
    @Getter
    private final Div objectDiv = new Div();
    private final TextArea entityDescription = new TextArea();
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
        entityDescription.setEnabled(false);
        entityDescription.setMinWidth(Setting.ENTITY_DESCRIPTION_MIN_WIDTH);
        entityDescription.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        objectDiv.add(entityDescription);
        add(new H2(Setting.DELETE_TEXT), objectDiv, createButtons());
    }

    private HorizontalLayout createButtons() {
        accept.setText(Setting.ACCEPT_BUTTON_TEXT);
        accept.addThemeVariants(Setting.SAVE_STYLE);
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
        remover.remove(removable).fold(
                aBoolean -> {
                    hornIntoNotificationAndLoggerInfo(SUCCESSFULLY_DELETED_USER_MESSAGE, removable);
                    close();
                    fireEvent(new UpdateEvent(this));
                    return aBoolean;
                },
                exception -> {
                    hornIntoNotificationAndLoggerInfo(exception.getMessage());
                    return false;
                }
        );
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
        entityDescription.setValue(removable.toDeleteString());
        objectDiv.setTitle(removable.toDeleteString());
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

    public static class UpdateEvent extends ComponentEvent<DeleteForm> {
        public UpdateEvent(DeleteForm source) {
            super(source, false);
        }
    }

    @Override
    public Logger log() {
        return log;
    }
}
