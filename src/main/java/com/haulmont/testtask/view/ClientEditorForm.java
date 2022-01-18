package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.ClientFieldAvailabilityChecker;
import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Validator;
import java.util.Optional;
import java.util.stream.Collectors;

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
        getPhoneNumberField().setReadOnly(true);
        getEmailField().setReadOnly(true);
        getPassportSeriesField().setReadOnly(true);
        getPassportNumberField().setReadOnly(true);
    }


    @Override
    protected void tuneBinderForImmutableFields() {
        /*
          this method override {@link CreateClientForm#tuneBinderForImmutableFields()}
          cause validator will be block fields
          passport
          phone
          email
          for being already in use.
         */
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
        if (!binder.writeBeanIfValid(build)) {
            BinderValidationStatus<Client> validate = binder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(ERROR_TEXT_DELIMITER));
            infoLabel.setText(ENUMERATION_ERRORS + errorText);
            return;
        }
        clientSaver.save(updatingClient.getClientId(), build)
                .fold(aBoolean -> {
                            Notification.show(UPDATING_MESSAGE, Setting.NOTIFICATION_DURATION, Setting.DEFAULT_POSITION);
                            log.info(Hornable.LOG_TEMPLATE_5,
                                    UPDATING_MESSAGE,
                                    LOG_DELIMITER,
                                    updatingClient.getClientId(),
                                    LOG_DELIMITER,
                                    build.toField());
                            close();
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
