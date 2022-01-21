package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.BankProvider;
import com.haulmont.testtask.backend.CreditConstraintProvider;
import com.haulmont.testtask.backend.CreditEditService;
import com.haulmont.testtask.backend.CreditSaver;
import com.haulmont.testtask.model.entity.Credit;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.notification.Notification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.haulmont.testtask.settings.ComponentSettings.DEFAULT_POSITION;
import static com.haulmont.testtask.settings.ComponentSettings.NOTIFICATION_DURATION;
import static com.haulmont.testtask.settings.Labels.NEW_CREDIT;
import static com.haulmont.testtask.settings.Labels.OLD_CREDIT;
import static com.haulmont.testtask.settings.Responses.*;


@Slf4j
public class CreditEditorForm extends CreateCreditForm {

    @Setter
    private Credit updatedCredit;

    private final CreditEditService creditEditService;

    public CreditEditorForm(BankProvider bankProvider, CreditSaver creditSaver, javax.validation.Validator validator, CreditEditService creditEditService, CreditConstraintProvider creditConstraintProvider) {
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
        Notification.show(TRYING_TO_EDIT_CREDIT, NOTIFICATION_DURATION, DEFAULT_POSITION);
        log.info(LOG_TEMPLATE_8,
                TRYING_TO_EDIT_CREDIT,
                LOG_DELIMITER,
                OLD_CREDIT,
                LOG_DELIMITER,
                updatedCredit.toDeleteString(),
                NEW_CREDIT,
                LOG_DELIMITER,
                newCredit.toDeleteString());

        creditEditService.edit(updatedCredit, newCredit)
                .fold(
                        aBoolean -> {
                            hornIntoNotificationAndLoggerInfo(SUCCESSFULLY_EDITED_USER_MESSAGE);
                            return aBoolean;
                        },
                        exception -> {
                            hornIntoNotificationAndLoggerInfo(exception.getMessage());
                            return false;
                        }
                );
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
