package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.ClientFieldAvailabilityChecker;
import com.haulmont.testtask.backend.ClientFieldsValidator;
import com.haulmont.testtask.backend.ClientSaver;
import com.haulmont.testtask.backend.excs.CreateClientException;
import com.haulmont.testtask.model.entity.Client;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.function.SerializablePredicate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.haulmont.testtask.view.Constant.DEFAULT_POSITION;
import static com.haulmont.testtask.view.Constant.NOTIFICATION_DURATION;

@PropertySource("application.yml")
@Slf4j
public class CreateClientForm extends FormLayout implements HasEvent, CanBeShown, CanBeSaved, CanBeClosed, CanBeCleared {

    private final ClientFieldsValidator clientFieldsValidator;
    private final ClientSaver clientSaver;

    @Getter(AccessLevel.PROTECTED)
    private Client client;

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

    public CreateClientForm(ClientFieldsValidator clientFieldsValidator, ClientSaver clientSaver, ClientFieldAvailabilityChecker clientFieldAvailabilityChecker) {
        this.clientFieldsValidator = clientFieldsValidator;
        this.clientSaver = clientSaver;
        this.clientFieldAvailabilityChecker = clientFieldAvailabilityChecker;
        tuneBinder();
        HorizontalLayout buttons = createButtons();
        tuneFields();

        add(new H3("Create new client"),
                new H3(""),
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
        client = Client.builder().build();
    }

    private HorizontalLayout createButtons() {
        tuneSaveButton();
        tuneClearButton();
        tuneCloseButton();
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        save.setEnabled(false);
        return new HorizontalLayout(save, clear, close);
    }

    private void tuneFields() {
        lastNameField.setLabel("Last name");
        lastNameField.setPlaceholder("Ivanov");
        lastNameField.setRequiredIndicatorVisible(true);

        nameField.setLabel("First name");
        nameField.setPlaceholder("Ivan");
        nameField.setRequiredIndicatorVisible(true);

        patronymicField.setLabel("Patronymic");
        patronymicField.setPlaceholder("Ivanovich");

        phoneNumberField.setLabel("Phone");
        phoneNumberField.setPlaceholder("(9**)***-**-**");
        phoneNumberField.setRequiredIndicatorVisible(true);

        emailField.setLabel("email");
        emailField.setPlaceholder("example@email.com");
        emailField.setRequiredIndicatorVisible(true);

        passportSeriesField.setLabel("passport series");
        passportSeriesField.setPlaceholder("0000");
        passportSeriesField.setMin(1000);
        passportSeriesField.setMax(9999);
        passportSeriesField.setRequiredIndicatorVisible(true);
        passportSeriesField.addValueChangeListener(event -> passportField.setValue(
                (event.getValue() == null ? "" : Integer.toString(event.getValue()))
                        + passportNumberField.getValue()
        ));

        passportNumberField.setLabel("passport number");
        passportNumberField.setPlaceholder("000000");
        passportNumberField.setMax(999999);
        passportNumberField.setMin(100000);
        passportNumberField.setRequiredIndicatorVisible(true);
        passportNumberField.addValueChangeListener(event -> passportField.setValue(
                passportSeriesField.getValue()
                        + (event.getValue() == null ? "" : Integer.toString(event.getValue()))
        ));

        passportField.setVisible(false);
    }

    private void tuneBinder() {
        SerializablePredicate<String> mustBeNotBlank = value -> value != null && !value.isBlank();
        SerializablePredicate<String> mustBeLetter =
                value -> value != null && value.equals(value.replace("\\W", "")
                        .replace("\\s", "")
                        .replace("\\d", "")
                );
        SerializablePredicate<String> mustBeLetterForPatronymic =
                value -> value == null || value.isEmpty() || value.equals(value.replace("\\W", "")
                        .replace("\\s", "")
                        .replace("\\d", "")
                );
        final String mustBeLetterError = "You can use only letter symbols";
        final String mustBeMinimumThreeSymbols = "Must be minimum three symbols";

        binder.forField(nameField)
                .withNullRepresentation("")
                .withValidator(mustBeNotBlank, "Name is required field")
                .withValidator(mustBeLetter, mustBeLetterError)
                .withValidator(new StringLengthValidator(mustBeMinimumThreeSymbols, 3, null))
                .bind(Client::getFirstName, Client::setFirstName);

        binder.forField(lastNameField)
                .withNullRepresentation("")
                .withValidator(mustBeNotBlank, "Last name is required field")
                .withValidator(mustBeLetter, mustBeLetterError)
                .withValidator(new StringLengthValidator(mustBeMinimumThreeSymbols, 3, null))
                .bind(Client::getLastName, Client::setLastName);

        binder.forField(patronymicField)
                .withNullRepresentation("")
                .withValidator(mustBeLetterForPatronymic, mustBeLetterError)
                .bind(Client::getPatronymic, Client::setPatronymic);

        binder.forField(phoneNumberField)
                .withNullRepresentation("")
                .withValidator(mustBeNotBlank, "Phone is required field")
                .withValidator((SerializablePredicate<String>) clientFieldsValidator::validatePhone, "not valid number, must be 10 digits starting with 9")
                .withValidator((SerializablePredicate<String>) clientFieldAvailabilityChecker::isAvailablePhone, "this phone is already in use")
                .bind(Client::getPhoneNumber, Client::setPhoneNumber);

        binder.forField(emailField)
                .withNullRepresentation("")
                .withValidator(mustBeNotBlank, "Email is required field")
                .withValidator(new EmailValidator("Incorrect email address"))
                .withValidator((SerializablePredicate<String>) clientFieldAvailabilityChecker::isAvailableEmail,
                        "this email is already in use")
                .bind(Client::getEmail, Client::setEmail);

        binder.forField(passportField)
                .withNullRepresentation("")
                .withValidator(mustBeNotBlank, "Passport is required field")
                .withValidator((SerializablePredicate<? super String>) s -> s.length() == 10, "must be 10 digits length")
                .withValidator(
                        (SerializablePredicate<String>) s -> s != null && s.equals(s.replaceAll("\\D", "")),
                        "Must be only digits in passport series and passport number.")
                .withValidator((SerializablePredicate<String>) clientFieldAvailabilityChecker::isAvailablePassport,
                        "this passport already in use")
                .bind(Client::getPassport, Client::setPassport);

    }

    @Override
    public void validateAndSave() {
        if (!binder.writeBeanIfValid(client)) {
            BinderValidationStatus<Client> validate = binder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(". "));
            infoLabel.setText("There are errors: " + errorText);
            return;
        }
        try {
            if (client.getPatronymic() == null) client.setPatronymic("");
            clientSaver.save(client);
            Notification.show("Saved bean values: " + client.toStringWithoutId(), NOTIFICATION_DURATION, DEFAULT_POSITION);
            log.info("Saved bean values: " + client.toString());
            close();
            clear();
        } catch (CreateClientException ex) {
            log.warn(ex.getMessage());
            infoLabel.setText(ex.getMessage());
            Notification.show(ex.getMessage(), NOTIFICATION_DURATION, DEFAULT_POSITION);
        }
    }

    @Override
    public void clear() {
        nameField.clear();
        lastNameField.clear();
        patronymicField.clear();
        phoneNumberField.clear();
        emailField.clear();

        passportSeriesField.clear();
        passportNumberField.clear();
        passportField.clear();

        client = Client.builder().build();
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
}
