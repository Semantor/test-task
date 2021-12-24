package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.BankFieldAvailabilityChecker;
import com.haulmont.testtask.backend.BankSaver;
import com.haulmont.testtask.backend.excs.CreateBankException;
import com.haulmont.testtask.model.entity.Bank;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;

import static com.haulmont.testtask.view.Constant.*;

@Slf4j
public class CreateBankForm extends FormLayout implements HasEvent, CanBeShown, CanBeClosed, CanBeSaved {
    private final BankSaver bankSaver;
    private final BankFieldAvailabilityChecker bankFieldAvailabilityChecker;

    private final TextField nameField = new TextField("enter name");

    private final Label infoLabel = new Label();

    private final Button save = new Button();
    private final Button close = new Button();

    public CreateBankForm(BankSaver bankSaver, BankFieldAvailabilityChecker bankFieldAvailabilityChecker) {
        this.bankSaver = bankSaver;
        this.bankFieldAvailabilityChecker = bankFieldAvailabilityChecker;
        setupFields();
        add(new H3("Create new Bank"), nameField, createButtons());
    }

    private void setupFields() {
        nameField.setAutofocus(true);
    }

    private HorizontalLayout createButtons() {
        tuneSaveButton();
        tuneCloseButton();
        return new HorizontalLayout(save, close);
    }

    @Override
    public void validateAndSave() {
        String nameFieldValue = nameField.getValue();
        if (nameFieldValue == null || nameFieldValue.isBlank()) {
            String response = "Name can not be empty";
            log.info("attempt to save bank is null or empty field");
            Notification.show(response, NOTIFICATION_DURATION, DEFAULT_POSITION);
            nameField.setInvalid(true);
            return;
        }
        if (!bankFieldAvailabilityChecker.isAvailableName(nameFieldValue)) {
            String response = "this name: `" + nameFieldValue+"` already in used";
            log.info(response);
            Notification.show(response, NOTIFICATION_DURATION, DEFAULT_POSITION);
            nameField.setInvalid(true);
            return;
        }
        nameField.setInvalid(false);
        String s = "trying to save new Bank with id: " + nameFieldValue;
        log.info(s);
        infoLabel.setText(s);
        Notification.show(s,NOTIFICATION_DURATION,DEFAULT_POSITION);
        try {
            bankSaver.save(Bank.builder().name(nameField.getValue()).build());
            s = "successfully save bank";
            infoLabel.setText(s);
            Notification.show(s,NOTIFICATION_DURATION,DEFAULT_POSITION);

            close();
        } catch (CreateBankException ex) {
            log.info(ex.getMessage());
            infoLabel.setText(ex.getMessage());
            Notification.show(ex.getMessage(), NOTIFICATION_DURATION, DEFAULT_POSITION);
        }

    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    public static class CloseEvent extends ComponentEvent<CreateBankForm> {
        public CloseEvent(CreateBankForm source) {
            super(source, false);
        }
    }

    @Override
    public void show() {
        nameField.setValue("");
    }

    @Override
    public Button getCloseButton() {
        return close;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }
}
