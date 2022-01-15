package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.ClientFieldAvailabilityChecker;
import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.backend.util.ConstraintViolationHandler;
import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.function.SerializablePredicate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.haulmont.testtask.Setting.*;

@Slf4j
public class CreateClientForm extends FormLayout implements HasEvent, CanBeShown, CanBeSaved, CanBeClosed, CanBeCleared, Hornable {


    private final Validator validator;
    private final ClientSaver clientSaver;
    private final ClientFieldAvailabilityChecker clientFieldAvailabilityChecker;

    @Getter(AccessLevel.PROTECTED)
    private final TextField lastNameField = new TextField();
    @Getter(AccessLevel.PROTECTED)
    private final TextField nameField = new TextField();
    @Getter(AccessLevel.PROTECTED)
    private final TextField patronymicField = new TextField();
    @Getter(AccessLevel.PROTECTED)
    private final TextField phoneNumberField = new TextField();
    @Getter(AccessLevel.PROTECTED)
    private final EmailField emailField = new EmailField();
    @Getter(AccessLevel.PROTECTED)
    private final IntegerField passportSeriesField = new IntegerField();
    @Getter(AccessLevel.PROTECTED)
    private final IntegerField passportNumberField = new IntegerField();
    @Getter(AccessLevel.PROTECTED)
    private final TextField passportField = new TextField();

    private final Label infoLabel = new Label();


    final Binder<Client> binder = new BeanValidationBinder<>(Client.class);

    private final Button save = new Button();
    private final Button clear = new Button();
    private final Button close = new Button();

    public CreateClientForm(Validator validator, ClientSaver clientSaver, ClientFieldAvailabilityChecker clientFieldAvailabilityChecker) {
        this.validator = validator;
        this.clientSaver = clientSaver;
        this.clientFieldAvailabilityChecker = clientFieldAvailabilityChecker;
        tuneBinder();
        HorizontalLayout buttons = createButtons();
        tuneFields();

        add(new H3(CREATE_NEW_CLIENT_H_3_LABEL),
                new Div(),
                lastNameField,
                nameField,
                patronymicField,
                phoneNumberField,
                emailField,
                passportSeriesField,
                passportNumberField,
                buttons,
                infoLabel);
    }

    public void show() {
        clear();
    }

    private HorizontalLayout createButtons() {
        tuneSaveButton();
        tuneClearButton();
        tuneCloseButton();
        return new HorizontalLayout(save, clear, close);
    }

    private void tuneFields() {
        lastNameField.setLabel(LAST_NAME_LABEL);
        lastNameField.setPlaceholder(LAST_NAME_PLACEHOLDER);
        lastNameField.setRequiredIndicatorVisible(true);

        nameField.setLabel(FIRST_NAME_LABEL);
        nameField.setPlaceholder(FIRST_NAME_PLACEHOLDER);
        nameField.setRequiredIndicatorVisible(true);

        patronymicField.setLabel(PATRONYMIC_LABEL);
        patronymicField.setPlaceholder(PATRONYMIC_PLACEHOLDER);

        phoneNumberField.setLabel(PHONE_NUMBER_LABEL);
        phoneNumberField.setPlaceholder(PHONE_NUMBER_PLACEHOLDER);
        phoneNumberField.setRequiredIndicatorVisible(true);

        emailField.setLabel(EMAIL_LABEL);
        emailField.setPlaceholder(EMAIL_PLACEHOLDER);
        emailField.setRequiredIndicatorVisible(true);

        passportSeriesField.setLabel(Setting.PASSPORT_SERIES_LABEL);
        passportSeriesField.setPlaceholder(PASSPORT_SERIES_PLACEHOLDER);
        passportSeriesField.setMin(PASSPORT_SERIES_MIN_VALUE);
        passportSeriesField.setMax(PASSPORT_SERIES_MAX_VALUE);
        passportSeriesField.setRequiredIndicatorVisible(true);
        passportSeriesField.addValueChangeListener(event -> passportField.setValue(
                (event.getValue() == null ? "" : Integer.toString(event.getValue()))
                        + passportNumberField.getValue()
        ));

        passportNumberField.setLabel(Setting.PASSPORT_NUMBER_LABEL);
        passportNumberField.setPlaceholder(PASSPORT_NUMBER_PLACEHOLDER);
        passportNumberField.setMax(PASSPORT_NUMBER_MAX_VALUE);
        passportNumberField.setMin(PASSPORT_NUMBER_MIN_VALUE);
        passportNumberField.setRequiredIndicatorVisible(true);
        passportNumberField.addValueChangeListener(event -> passportField.setValue(
                passportSeriesField.getValue()
                        + (event.getValue() == null ? "" : Integer.toString(event.getValue()))
        ));

        passportField.setVisible(false);
    }

    private void tuneBinder() {

        binder.forField(nameField)
                .withNullRepresentation(NAME_FIELD_DEFAULT_VALUE)
                .withValidator(predict(CLIENT_FIRST_NAME_FOR_SORTING), Setting.UNIVERSAL_NAME_ERROR)
                .bind(Client::getFirstName, Client::setFirstName);

        binder.forField(lastNameField)
                .withNullRepresentation(NAME_FIELD_DEFAULT_VALUE)
                .withValidator(predict(CLIENT_LAST_NAME_FOR_SORTING), Setting.UNIVERSAL_NAME_ERROR)
                .bind(Client::getLastName, Client::setLastName);

        binder.forField(patronymicField)
                .withNullRepresentation(NAME_FIELD_DEFAULT_VALUE)
                .withValidator(predict(CLIENT_PATRONYMIC_FOR_SORTING), Setting.PATRONYMIC_ERROR)
                .bind(Client::getPatronymic, Client::setPatronymic);

        binder.forField(phoneNumberField)
                .withNullRepresentation(PHONE_FIELD_DEFAULT_VALUE)
                .withValidator(predict(CLIENT_PHONE_NUMBER_FOR_SORTING), NO_VALID_PHONE_ERROR_MSG)
                .withValidator((SerializablePredicate<String>) clientFieldAvailabilityChecker::isAvailablePhone,
                        PHONE_ALREADY_IN_USE_ERROR_MSG)
                .bind(Client::getPhoneNumber, Client::setPhoneNumber);

        binder.forField(emailField)
                .withNullRepresentation(EMAIL_FIELD_DEFAULT_VALUE)
                .withValidator(predict(CLIENT_EMAIL_FOR_SORTING), INCORRECT_EMAIL_ADDRESS_ERROR_MSG)
                .withValidator((SerializablePredicate<String>) clientFieldAvailabilityChecker::isAvailableEmail,
                        EMAIL_ALREADY_IN_USE_ERROR_MSG)
                .bind(Client::getEmail, Client::setEmail);

        binder.forField(passportField)
                .withNullRepresentation(PASSPORT_FIELD_DEFAULT_VALUE)
                .withValidator(predict(CLIENT_PASSPORT_FOR_SORTING), NO_VALID_PASSPORT)
                .withValidator((SerializablePredicate<String>) clientFieldAvailabilityChecker::isAvailablePassport,
                        PASSPORT_ALREADY_IN_USE_ERROR_MSG)
                .bind(Client::getPassport, Client::setPassport);

    }

    @NotNull
    private SerializablePredicate<String> predict(String fieldName) {
        return s -> {
            Set<ConstraintViolation<Client>> constraintViolations = validator.validateValue(Client.class, fieldName, s);
            log.info(ConstraintViolationHandler.handleToString(constraintViolations));
            return constraintViolations.isEmpty();
        };
    }

    @Override
    public void validateAndSave() {
        Client newBorn = Client.builder().build();
        if (!binder.writeBeanIfValid(newBorn)) {
            BinderValidationStatus<Client> validate = binder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(ERROR_TEXT_DELIMITER));
            infoLabel.setText(ENUMERATION_ERRORS + errorText);
            return;
        }

        if (newBorn.getPatronymic() == null) newBorn.setPatronymic(NAME_FIELD_DEFAULT_VALUE);
        clientSaver.save(newBorn)
                .fold(aBoolean -> {
                            hornIntoNotificationAndLoggerInfo(SUCCESSFULLY_SAVED_USER_MESSAGE, newBorn);
                            close();
                            clear();
                            return aBoolean;
                        },
                        exception -> {
                            hornIntoNotificationAndLoggerInfo(exception.getMessage());
                            return false;
                        }
                );
    }

    @Override
    public void clear() {
        nameField.clear();
        nameField.setInvalid(false);
        lastNameField.clear();
        lastNameField.setInvalid(false);
        patronymicField.clear();
        patronymicField.setInvalid(false);
        phoneNumberField.clear();
        phoneNumberField.setInvalid(false);
        emailField.clear();
        emailField.setInvalid(false);

        passportSeriesField.clear();
        passportField.setInvalid(false);
        passportNumberField.clear();
        passportField.setInvalid(false);
        passportField.clear();

        infoLabel.setText("");
    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    public static class CloseEvent extends ComponentEvent<CreateClientForm> {
        public CloseEvent(CreateClientForm source) {
            super(source, false);
        }
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    @Override
    public Button getCloseButton() {
        return close;
    }

    @Override
    public Button getClearButton() {
        return clear;
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
