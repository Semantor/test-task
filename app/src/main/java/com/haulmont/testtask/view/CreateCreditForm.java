package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.BankProvider;
import com.haulmont.testtask.backend.CreditConstraintProvider;
import com.haulmont.testtask.backend.CreditSaver;
import com.haulmont.testtask.backend.util.ConstraintViolationHandler;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Credit;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static com.haulmont.testtask.settings.ErrorMessages.*;
import static com.haulmont.testtask.settings.FieldNameSettings.CREDIT_LIMIT_FIELD_NAME;
import static com.haulmont.testtask.settings.FieldNameSettings.CREDIT_RATE_FIELD_NAME;
import static com.haulmont.testtask.settings.Labels.*;
import static com.haulmont.testtask.settings.Responses.SUCCESSFULLY_SAVED_USER_MESSAGE;
import static com.haulmont.testtask.settings.Responses.TRYING_TO_SAVE_NEW_CREDIT;


@Slf4j
public class CreateCreditForm extends FormLayout implements HasEvent, CanBeShown, CanBeSaved, CanBeCleared, CanBeClosed, Hornable {


    protected final BankProvider bankProvider;
    protected final CreditSaver creditSaver;
    protected final javax.validation.Validator validator;
    protected final CreditConstraintProvider creditConstraintProvider;


    @Getter(AccessLevel.PROTECTED)
    private final ComboBox<Bank> bankComboBox = new ComboBox<>();
    @Getter(AccessLevel.PROTECTED)
    private final BigDecimalField creditLimitField = new BigDecimalField();
    @Getter(AccessLevel.PROTECTED)
    private final BigDecimalField creditRateField = new BigDecimalField();

    @Getter(AccessLevel.PROTECTED)
    private final Binder<Credit> binder = new Binder<>(Credit.class);

    private final Button save = new Button();
    private final Button clear = new Button();
    private final Button close = new Button();

    public CreateCreditForm(BankProvider bankProvider, CreditSaver creditSaver, javax.validation.Validator validator1, CreditConstraintProvider creditConstraintProvider) {
        this.creditSaver = creditSaver;
        this.bankProvider = bankProvider;
        this.validator = validator1;
        this.creditConstraintProvider = creditConstraintProvider;
        tuneFields();
        tuneBinder();
        add(bankComboBox, creditLimitField, creditRateField, createButtons());
    }

    public void show() {
        clear();
    }

    private void tuneFields() {
        bankComboBox.setLabel(BANK_LABEL);
        bankComboBox.setItemLabelGenerator(Bank::toField);

        creditLimitField.setValue(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE);
        creditLimitField.setLabel(CREDIT_LIMIT_LABEL);
        creditLimitField.setPrefixComponent(new Icon(VaadinIcon.DOLLAR));

        creditRateField.setValue(BigDecimal.TEN);
        creditRateField.setLabel(CREDIT_RATE_LABEL);
        creditRateField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        creditRateField.setSuffixComponent(new Icon(VaadinIcon.BOOK_PERCENT));
    }


    private void tuneBinder() {
        binder.forField(bankComboBox)
                .withValidator(Objects::nonNull, MUST_CHOOSE_BANK_ERROR_MSG)
                .bind(Credit::getBank, Credit::setBank);
        binder.forField(creditLimitField)
                .withValidator(predict(CREDIT_LIMIT_FIELD_NAME), WRONG_CREDIT_LIMIT_MESSAGE)
                .bind(Credit::getCreditLimit, Credit::setCreditLimit);
        binder.forField(creditRateField)
                .withValidator(predict(CREDIT_RATE_FIELD_NAME), WRONG_CREDIT_RATE_MESSAGE)
                .bind(Credit::getCreditRate, Credit::setCreditRate);
    }

    private <T> SerializablePredicate<T> predict(String fieldName) {
        return s -> {
            Set<ConstraintViolation<Credit>> constraintViolations = validator.validateValue(Credit.class, fieldName, s);
            log.info(ConstraintViolationHandler.handleToString(constraintViolations));
            return constraintViolations.isEmpty();
        };
    }

    private HorizontalLayout createButtons() {
        tuneCloseButton();
        tuneSaveButton();
        tuneClearButton();
        return new HorizontalLayout(save, clear, close);
    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    @Override
    public void clear() {
        creditLimitField.setValue(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE);
        creditRateField.setValue(BigDecimal.TEN);
        bankComboBox.setItems(bankProvider.getAllBanks());
    }

    public void validateAndSave() {
        Credit credit = Credit.builder().build();
        if (binder.writeBeanIfValid(credit)) {
            hornIntoNotificationAndLoggerInfo(TRYING_TO_SAVE_NEW_CREDIT, credit);
            creditSaver.save(credit).fold(
                    aBoolean -> {
                        hornIntoNotificationAndLoggerInfo(SUCCESSFULLY_SAVED_USER_MESSAGE, credit);
                        close();
                        clear();
                        return aBoolean;
                    },
                    exception -> {
                        hornIntoNotificationAndLoggerInfo(exception.getMessage(), credit);
                        return false;
                    }
            );
        }
    }

    public static class CloseEvent extends ComponentEvent<CreateCreditForm> {
        public CloseEvent(CreateCreditForm source) {
            super(source, false);
        }
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    @Override
    public Button getSaveButton() {
        return save;
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
    public Logger log() {
        return log;
    }
}
