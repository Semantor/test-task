package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.ClientFieldAvailabilityChecker;
import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Validator;

import static com.haulmont.testtask.Setting.*;

@Slf4j
public class ClientEditorForm extends CreateClientForm {

    @Setter
    private transient Client updatingClient;
    private final transient ClientSaver clientSaver;

    public ClientEditorForm(Validator validator,
                            ClientSaver clientSaver,
                            ClientFieldAvailabilityChecker clientFieldAvailabilityChecker) {
        super(validator, clientSaver, clientFieldAvailabilityChecker);
        this.clientSaver = clientSaver;
        getClearButton().setVisible(false);
        tuneImmutableFields();
    }

    private void fillField() {
        getNameField().setValue(updatingClient.getFirstName());
        getLastNameField().setValue(updatingClient.getLastName());
        getPatronymicField().setValue(updatingClient.getPatronymic());
        getPhoneNumberField().setValue(updatingClient.getPhoneNumber());
        getEmailField().setValue(updatingClient.getEmail());
        String series = updatingClient.getPassport().substring(Setting.PASSPORT_SERIES_START_INDEX, PASSPORT_NUMBER_START_INDEX);
        String number = updatingClient.getPassport().substring(PASSPORT_NUMBER_START_INDEX);
        getPassportSeriesField().setValue(Integer.parseInt(series));
        getPassportNumberField().setValue(Integer.parseInt(number));
    }

    private void tuneImmutableFields() {
        getPhoneNumberField().setReadOnly(true);
        // the reason is to drop validating on immutable fields
        binder.forField(getPhoneNumberField()).bind(Client::getPhoneNumber, Client::setPhoneNumber);
        getEmailField().setReadOnly(true);
        binder.forField(getEmailField()).bind(Client::getEmail, Client::setEmail);
        getPassportSeriesField().setReadOnly(true);
        getPassportNumberField().setReadOnly(true);
    }

    /**
     * MUST BE {@link #setUpdatingClient(Client)} FIRST
     */
    @Override
    public void show() {
        fillField();
    }

    @Override
    public void validateAndSave() {
        Client build = Client.builder().build();
        super.binder.writeBeanIfValid(build);

        clientSaver.save(updatingClient.getClientId().toString(), build)
                .fold(aBoolean -> {
                            Notification.show(UPDATING_MESSAGE, Setting.NOTIFICATION_DURATION, Setting.DEFAULT_POSITION);
                            log.info(Hornable.LOG_TEMPLATE_5,
                                    UPDATING_MESSAGE,
                                    LOG_DELIMITER,
                                    updatingClient.getClientId(),
                                    LOG_DELIMITER,
                                    build.toField());
                            return aBoolean;
                        },
                        exception -> {
                            hornIntoNotificationAndLoggerInfo(exception.getMessage());
                            return false;
                        }
                );
    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    public static class CloseEvent extends ComponentEvent<ClientEditorForm> {
        public CloseEvent(ClientEditorForm source) {
            super(source, false);
        }
    }

    @Override
    public void clear() {
        fillField();
    }
}
