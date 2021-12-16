package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.CreditEditService;
import com.haulmont.testtask.backend.BankProvider;
import com.haulmont.testtask.backend.CreditSaver;
import com.haulmont.testtask.backend.Validator;
import com.haulmont.testtask.backend.excs.CreditDeleteException;
import com.haulmont.testtask.model.entity.Credit;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreditEditorForm extends CreateCreditForm {

    @Setter
    private Credit updatedCredit;

    private final CreditEditService creditEditService;

    public CreditEditorForm(BankProvider bankProvider, CreditSaver creditSaver, Validator validator, CreditEditService creditEditService) {
        super(bankProvider, creditSaver, validator);
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
        String tryingMsg = "trying to edit credit\n old credit: " + updatedCredit.toDeleteString() + "\nnew credit: " + newCredit.toDeleteString();
        horn(tryingMsg);
        try {
            creditEditService.edit(updatedCredit, newCredit);
            String successfulMsg = "Successfully edit!";
            horn(successfulMsg);
        } catch (CreditDeleteException exception) {
            horn(exception.getMessage());
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
