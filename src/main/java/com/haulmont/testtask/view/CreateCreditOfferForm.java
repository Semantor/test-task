package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.*;
import com.haulmont.testtask.backend.excs.CreateCreditOfferException;
import com.haulmont.testtask.backend.excs.IllegalArgumentExceptionWithoutStackTrace;
import com.haulmont.testtask.model.entity.Client;
import com.haulmont.testtask.model.entity.Credit;
import com.haulmont.testtask.model.entity.CreditOffer;
import com.haulmont.testtask.model.entity.Payment;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.haulmont.testtask.Setting.*;

@PropertySource("config.properties")
@Slf4j
public class CreateCreditOfferForm extends FormLayout implements HasEvent, CanBeShown, CanBeClosed, CanBeCleared, CanBeSaved, Hornable {

    private final List<Integer> DEFAULT_MONTH_COUNT = Arrays.asList(6, 12, 24, 36);

    private final CreditOfferCreator creditOfferCreator;
    private final CreditProvider creditProvider;
    private final PaymentCalculator paymentCalculator;
    @Getter
    private final PaymentGridLayout paymentGridLayout;
    private final Validator validator;
    private final CreditConstraintProvider creditConstraintProvider;

    @Getter
    @Setter
    private Client client;
    private CreditOffer creditOffer;

    private final TextField clientField = new TextField();
    private final ComboBox<Credit> credit = new ComboBox<>();
    private final BigDecimalField amountField = new BigDecimalField();
    private final ComboBox<Integer> month = new ComboBox<>();
    private final DatePicker datePicker = new DatePicker();

    private final Label infoLabel = new Label();

    private final Binder<CreditOffer> binder = new BeanValidationBinder<>(CreditOffer.class);

    private final Button calculate = new Button();
    private final Button save = new Button();
    private final Button clear = new Button();
    private final Button close = new Button();


    @Autowired
    public CreateCreditOfferForm(CreditOfferCreator coc, CreditProvider cp, PaymentCalculator paymentCalculator, PaymentGridLayout paymentGridLayout, Validator validator, CreditConstraintProvider creditConstraintProvider) {
        this.validator = validator;
        this.creditOfferCreator = coc;
        this.creditProvider = cp;
        this.paymentCalculator = paymentCalculator;
        this.paymentGridLayout = paymentGridLayout;
        this.creditConstraintProvider = creditConstraintProvider;

        tuneBinder();
        HorizontalLayout buttons = createButtonsLayout();
        paymentGridLayout.setVisible(false);
        tuneFields();

        handleChangeListener();

        add(clientField, credit, amountField, month, datePicker, paymentGridLayout, buttons);
    }

    private void handleChangeListener() {
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    }


    /**
     * MUST BE {@link #setClient(Client)} FIRST!
     */
    @Override
    public void show() {
        clientField.setValue(client.toField());
        credit.setItems(creditProvider.getAllCredit());
        clear();
    }

    private HorizontalLayout createButtonsLayout() {
        calculate.setText(Setting.CALCULATE_BUTTON_TEXT);
        calculate.addThemeVariants(Setting.CALCULATE_STYLE);
        calculate.addClickListener(event -> calculate());
        tuneSaveButton();
        tuneCloseButton();
        tuneClearButton();

        return new HorizontalLayout(calculate, close, save, clear);
    }

    @Override
    public void validateAndSave() {
        LocalDate firstPaymentDate = paymentGridLayout.getPayments().get(0).getDate();
        CreditOffer builtCreditOffer = CreditOffer.builder()
                .client(client)
                .credit(creditOffer.getCredit())
                .creditAmount(creditOffer.getCreditAmount())
                .monthCount(creditOffer.getMonthCount())
                .build();
        List<Payment> calculatingPayment = paymentCalculator.calculate(builtCreditOffer.getCredit(), builtCreditOffer, builtCreditOffer.getMonthCount(), builtCreditOffer.getCreditAmount(), firstPaymentDate);
        if (calculatingPayment.isEmpty()) {
            hornIntoNotificationAndLoggerInfo(WRONG_INCOME_DATA, builtCreditOffer);
        }

        builtCreditOffer.setPayments(calculatingPayment);
        try {
            creditOfferCreator.save(builtCreditOffer);
            hornIntoNotificationAndLoggerInfo(SUCCESSFULLY_SAVED_USER_MESSAGE, builtCreditOffer);
            clear();
            close();
        } catch (CreateCreditOfferException ex) {
            hornIntoNotificationAndLoggerInfo(ex.getMessage());
        }
    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    private void calculate() {
        LocalDate dateOfReceiving = datePicker.getValue();
        if (dateOfReceiving.isBefore(LocalDate.now())) {
            infoLabel.setText(Setting.WRONG_DATE_IS_PICKED);
            hornIntoNotificationAndLoggerInfo(WRONG_DATE_IS_PICKED);
            return;
        }
        try {
            binder.writeBean(creditOffer);
            paymentGridLayout.setVisible(true);
            save.setVisible(true);
            clear.setVisible(true);
            paymentGridLayout.setPayments(paymentCalculator.calculate(creditOffer.getCredit(),
                    creditOffer.getMonthCount(), creditOffer.getCreditAmount(), dateOfReceiving));

            paymentGridLayout.show();
        } catch (ValidationException ex) {
            infoLabel.setText(ex.getMessage());
            hornIntoNotificationAndLoggerInfo(ex.getMessage());

        }
    }

    @Override
    public void clear() {
        credit.setItems(creditProvider.getAllCredit());
        amountField.setValue(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE);
        creditOffer = CreditOffer.builder().client(client).build();
        save.setVisible(false);
        clear.setVisible(false);
        paymentGridLayout.setVisible(false);
        paymentGridLayout.setPayments(Collections.emptyList());
        paymentGridLayout.show();
    }

    private void tuneFields() {
        clientField.setLabel(CLIENT_LABEL);
        clientField.setEnabled(false);


        credit.setLabel(CREDIT_LABEL);
        credit.setItems(Collections.emptyList());
        credit.setItemLabelGenerator((ItemLabelGenerator<Credit>) Credit::toField);

        amountField.setLabel(AMOUNT_LABEL);
        amountField.setSuffixComponent(new Icon(VaadinIcon.DOLLAR));

        month.setLabel(MONTH_LABEL);
        month.setItems(DEFAULT_MONTH_COUNT);
        month.addCustomValueSetListener(event -> {
            int k = DEFAULT_MONTH_COUNT.get(0);
            try {
                k = Integer.parseInt(event.getDetail());
            } catch (NumberFormatException ex) {
                log.warn(WRONG_MONTH_COUNT_INPUT);
            }
            month.setValue(k);
        });
        month.setRequired(true);
        month.setHelperText(ENTER_CUSTOM_MONTH_COUNT_HELPER_TEXT);
        datePicker.setLabel(RECEIVING_DATE_LABEL);
    }

    private void tuneBinder() {
        binder.forField(credit)
                .bind(CreditOffer::getCredit, CreditOffer::setCredit);
        binder.forField(amountField)
                .withValidator(
                        bigDecimal -> credit.getValue() != null &&
                                validator.validateCreditAmount(
                                        bigDecimal,
                                        credit.getValue().getCreditLimit()),
                        IllegalArgumentExceptionWithoutStackTrace.
                                amountErrorMsg(creditConstraintProvider.CREDIT_LIMIT_MIN_VALUE,
                                        creditConstraintProvider.CREDIT_LIMIT_MAX_VALUE)
                ).bind(CreditOffer::getCreditAmount, CreditOffer::setCreditAmount);

        binder.forField(month)
                .withValidator(integer -> integer != null && integer > 0, Setting.MUST_BE_MORE_THAN_ZERO)
                .bind(CreditOffer::getMonthCount, CreditOffer::setMonthCount);
    }


    public static class CloseEvent extends ComponentEvent<CreateCreditOfferForm> {
        public CloseEvent(CreateCreditOfferForm source) {
            super(source, false);
        }
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }


    @Override
    public Button getClearButton() {
        return clear;
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
