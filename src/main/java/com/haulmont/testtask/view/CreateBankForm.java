package com.haulmont.testtask.view;

import com.haulmont.testtask.backend.BankSaver;
import com.haulmont.testtask.model.entity.Bank;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class CreateBankForm extends FormLayout implements HasEvent, CanBeShown, CanBeClosed, CanBeSaved {
    private final BankSaver bankSaver;

    private final ComboBox<UUID> uuidComboBox = new ComboBox<>();

    private final Label infoLabel = new Label();

    private final Button save = new Button();
    private final Button close = new Button();

    public CreateBankForm(BankSaver bankSaver) {
        this.bankSaver = bankSaver;
        setupFields();
        add(new H3("Create new Bank"), uuidComboBox, createButtons());
    }

    private void setupFields() {
        uuidComboBox.setLabel("choose one uuid");
        uuidComboBox.setItems(generateUUID());
    }

    private HorizontalLayout createButtons() {
        tuneSaveButton();
        tuneCloseButton();
        return new HorizontalLayout(save, close);
    }

    @Override
    public void validateAndSave() {
        String s = "trying to save new Bank with id: " + uuidComboBox.getValue();
        log.info(s);
        infoLabel.setText(s);
        bankSaver.save(Bank.builder().bankId(uuidComboBox.getValue()).build());

    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    public static class CloseEvent extends ComponentEvent<CreateBankForm> {
        public CloseEvent(CreateBankForm source) {
            super(source, false);
        }
    }

    private List<UUID> generateUUID() {
        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            uuidList.add(UUID.randomUUID());
        }
        return uuidList;
    }

    @Override
    public void show() {
        this.uuidComboBox.setItems(generateUUID());
    }

    @Override
    public Button getCloseButton() {
        return close;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }
}
