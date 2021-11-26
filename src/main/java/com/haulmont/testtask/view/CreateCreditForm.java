package com.haulmont.testtask.view;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.backend.BankProvider;
import com.haulmont.testtask.backend.CreditSaver;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Bank;
import com.haulmont.testtask.model.entity.Credit;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

import static com.haulmont.testtask.view.Constant.DEFAULT_POSITION;
import static com.haulmont.testtask.view.Constant.NOTIFICATION_DURATION;

@Slf4j
public class CreateCreditForm extends FormLayout implements HasEvent, CanBeShown, CanBeSaved, CanBeCleared, CanBeClosed {
    private static final String WRONG_CREDIT_LIMIT_MESSAGE = "wrong credit limit value";
    private static final String WRONG_CREDIT_RATE_MESSAGE = "wrong credit rate value";

    private final BankProvider bankProvider;
    private final CreditSaver creditSaver;
    private final Validator validator;

    private Credit credit;

    private final ComboBox<Bank> bankComboBox = new ComboBox<>();
    private final BigDecimalField creditLimitField = new BigDecimalField();
    private final BigDecimalField creditRateField = new BigDecimalField();

    private final Binder<Credit> binder = new Binder<>(Credit.class);

    private final Button save = new Button();
    private final Button clear = new Button();
    private final Button close = new Button();

    public CreateCreditForm(BankProvider bankProvider, CreditSaver creditSaver, Validator validator) {
        this.validator = validator;
        this.creditSaver = creditSaver;
        this.bankProvider = bankProvider;
        tuneFields();
        tuneBinder();
        add(bankComboBox, creditLimitField, creditRateField, createButtons());
    }

    public void show() {
        creditLimitField.setValue(Config.CREDIT_LIMIT_MIN_VALUE);
        creditRateField.setValue(BigDecimal.TEN);
        credit = Credit.builder().build();
        bankComboBox.setItems(bankProvider.getAllBanks());
    }

    private void tuneFields() {
        bankComboBox.setLabel("bank");
        bankComboBox.setItemLabelGenerator(Bank::toString);

        creditLimitField.setValue(Config.CREDIT_LIMIT_MIN_VALUE);
        creditLimitField.setLabel("credit limit");
        creditLimitField.setPrefixComponent(new Icon(VaadinIcon.DOLLAR));

        creditRateField.setValue(BigDecimal.TEN);
        creditRateField.setLabel("credit rate");
        creditRateField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        creditRateField.setSuffixComponent(new Icon(VaadinIcon.BOOK_PERCENT));
    }


    private void tuneBinder() {
        SerializablePredicate<BigDecimal> predicateForCreditLimitField =
                creditAmount ->
                        !creditLimitField.isEmpty() &&
                                creditAmount.scale() < 3 &&
                                validator.validateCreditAmount(creditAmount);

        SerializablePredicate<BigDecimal> predicateForCreditRateField =
                creditRate ->
                        !creditRateField.isEmpty() &&
                                creditRate.scale() < 3 &&
                                validator.validateCreditRate(creditRate);


        binder.forField(bankComboBox)
                .withValidator(Objects::nonNull, "must choose bank")
                .bind(Credit::getBank, Credit::setBank);
        binder.forField(creditLimitField)
                .withValidator(predicateForCreditLimitField, WRONG_CREDIT_LIMIT_MESSAGE)
                .bind(Credit::getCreditLimit, Credit::setCreditLimit);
        binder.forField(creditRateField)
                .withValidator(predicateForCreditRateField, WRONG_CREDIT_RATE_MESSAGE)
                .bind(Credit::getCreditRate, Credit::setCreditRate);
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
        creditLimitField.setValue(Config.CREDIT_LIMIT_MIN_VALUE);
        creditRateField.setValue(BigDecimal.TEN);
        credit = Credit.builder().build();
        bankComboBox.setItems(bankProvider.getAllBanks());
    }

    public void validateAndSave() {
        if (binder.writeBeanIfValid(credit)) {
            String msg1 = "trying to save credit: " + credit;
            log.info(msg1);
            Notification.show(msg1, NOTIFICATION_DURATION, DEFAULT_POSITION);
            creditSaver.save(credit);
            String msg2 = "Successfully saved credit: " + credit;
            log.info(msg2);
            Notification.show(msg2, NOTIFICATION_DURATION, DEFAULT_POSITION);
            close();
            clear();
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
}
