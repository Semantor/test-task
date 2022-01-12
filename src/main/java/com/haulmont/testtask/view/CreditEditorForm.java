package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.*;
import com.haulmont.testtask.backend.excs.CreditDeleteException;
import com.haulmont.testtask.model.entity.Credit;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.haulmont.testtask.Setting.LOG_DELIMITER;

@Slf4j
public class CreditEditorForm extends CreateCreditForm {

    @Setter
    private Credit updatedCredit;

    private final CreditEditService creditEditService;

    public CreditEditorForm(BankProvider bankProvider, CreditSaver creditSaver, Validator validator, CreditEditService creditEditService, CreditConstraintProvider creditConstraintProvider) {
        super(bankProvider, creditSaver, validator, creditConstraintProvider);
        this.creditEditService = creditEditService;
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
        Credit newCredit = Credit.builder().build();
        getBinder().writeBeanIfValid(newCredit);
        Notification.show(Setting.TRYING_TO_EDIT_CREDIT, Setting.NOTIFICATION_DURATION, Setting.DEFAULT_POSITION);
        log.info(LOG_TEMPLATE_8,
                Setting.TRYING_TO_EDIT_CREDIT,
                LOG_DELIMITER,
                Setting.OLD_CREDIT,
                LOG_DELIMITER,
                updatedCredit.toDeleteString(),
                Setting.NEW_CREDIT,
                LOG_DELIMITER,
                newCredit.toDeleteString());
        try {
            creditEditService.edit(updatedCredit, newCredit);
            hornIntoNotificationAndLoggerInfo(Setting.SUCCESSFULLY_EDITED_USER_MESSAGE);
        } catch (CreditDeleteException exception) {
            hornIntoNotificationAndLoggerInfo(exception.getMessage());
        }
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
