package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.BankFieldAvailabilityChecker;
import com.haulmont.testtask.backend.BankSaver;
import com.haulmont.testtask.backend.excs.CreateBankException;
import com.haulmont.testtask.model.entity.Bank;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import static com.haulmont.testtask.Setting.*;

@Slf4j
public class CreateBankForm extends FormLayout implements HasEvent, CanBeShown, CanBeClosed, CanBeSaved, Hornable {
    private final BankSaver bankSaver;
    private final BankFieldAvailabilityChecker bankFieldAvailabilityChecker;

    private final TextField nameField = new TextField(Setting.CREATE_BANK_FORM_NAME_FIELD_LABEL);


    private final Button save = new Button();
    private final Button close = new Button();


    public CreateBankForm(BankSaver bankSaver, BankFieldAvailabilityChecker bankFieldAvailabilityChecker) {
        this.bankSaver = bankSaver;
        this.bankFieldAvailabilityChecker = bankFieldAvailabilityChecker;
        setupFields();
        add(new H3(Setting.CREATE_BANK_FORM_H_3_LABEL), nameField, createButtons());
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
        String infoMsg;
        if (nameFieldValue == null || nameFieldValue.isBlank()) {
            log.info(ATTEMPT_TO_SAVE_EMPTY_FIELD);
            Notification.show(NAME_CAN_NOT_BE_EMPTY, NOTIFICATION_DURATION, DEFAULT_POSITION);
            nameField.setInvalid(true);
            return;
        }
        if (!bankFieldAvailabilityChecker.isAvailableName(nameFieldValue)) {
            infoMsg = NAME_IS_ALREADY_EXISTS + nameFieldValue;
            log.info(infoMsg);
            Notification.show(infoMsg, NOTIFICATION_DURATION, DEFAULT_POSITION);
            nameField.setInvalid(true);
            return;
        }
        nameField.setInvalid(false);
        hornIntoNotificationAndLoggerInfo(TRYING_TO_SAVE_NEW_BANK, nameFieldValue);
        try {
            bankSaver.save(Bank.builder().name(nameField.getValue()).build());
            hornIntoNotificationAndLoggerInfo(SUCCESSFULLY_SAVED_USER_MESSAGE);
            close();
        } catch (CreateBankException ex) {
            hornIntoNotificationAndLoggerInfo(ex.getMessage());
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
        nameField.setValue(NAME_FIELD_DEFAULT_VALUE);
    }

    @Override
    public Button getCloseButton() {
        return close;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }

    @Override
    public Logger log() {
        return log;
    }
}
