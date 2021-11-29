package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientEditorForm extends CreateClientForm {

    @Setter
    private Client updatingClient;
    private final ClientSaver clientSaver;

    public ClientEditorForm(ClientFieldsValidator clientFieldsValidator, ClientSaver clientSaver) {
        super(clientFieldsValidator, clientSaver);
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
        String series = updatingClient.getPassport().substring(0, 4);
        String number = updatingClient.getPassport().substring(4);
        getPassportSeriesField().setValue(Integer.parseInt(series));
        getPassportNumberField().setValue(Integer.parseInt(number));
    }

    private void tuneImmutableFields() {
        getPhoneNumberField().setReadOnly(true);
        getEmailField().setReadOnly(true);
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
         try {
             clientSaver.save(updatingClient.getClientId().toString(), build);
             String s ="update client to " + build.toField();
             Notification.show(s,Constant.NOTIFICATION_DURATION,Constant.DEFAULT_POSITION);
             log.info(s);
         }catch (CreateClientException ex){
             Notification.show(ex.getMessage(),Constant.NOTIFICATION_DURATION,Constant.DEFAULT_POSITION);
             log.info(ex.getMessage());
         }
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
