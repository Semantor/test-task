package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.BankProvider;
import com.haulmont.testtask.backend.CreditSaver;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.model.entity.Credit;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreditEditorForm extends CreateCreditForm {

    @Setter
    private Credit updatedCredit;
    private final CreditSaver creditSaver;

    public CreditEditorForm(BankProvider bankProvider, CreditSaver creditSaver, Validator validator) {
        super(bankProvider, creditSaver, validator);
        this.creditSaver = creditSaver;
        tuneImmutableFields();
    }

    private void tuneImmutableFields() {
        getBankComboBox().setEnabled(false);
    }

    void fillField() {
        getBankComboBox().setItems(updatedCredit.getBank());
        getBankComboBox().setValue(updatedCredit.getBank());
        getCreditLimitField().setValue(updatedCredit.getCreditLimit());
        getCreditRateField().setValue(updatedCredit.getCreditRate());
    }

    @Override
    public void validateAndSave() {
        Credit credit = Credit.builder().build();
        getBinder().writeBeanIfValid(credit);
        creditSaver.save(credit);
        String s = "credit save:" + credit;
        Notification.show(s, Constant.NOTIFICATION_DURATION, Constant.DEFAULT_POSITION);
        log.info(s);
        close();
    }

    @Override
    public void clear() {
        fillField();
    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    public static class CloseEvent extends ComponentEvent<CreditEditorForm> {
        public CloseEvent(CreditEditorForm source) {
            super(source, false);
        }
    }

    /**
     * USE {@link #setUpdatedCredit(Credit)} FIRST
     */
    @Override
    public void show() {
        fillField();
    }
}
