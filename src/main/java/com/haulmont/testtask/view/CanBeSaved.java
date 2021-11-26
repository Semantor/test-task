package com.haulmont.testtask.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;

public interface CanBeSaved {
    Button getSaveButton();

    default  void tuneSaveButton(){
        Button save = getSaveButton();
        save.addClickShortcut(Key.ENTER);
        save.setText(Constant.SAVE_TEXT);
        save.addThemeVariants(Constant.SAVE_STYLE);
        save.addClickListener(event -> validateAndSave());
    }

    void validateAndSave();
}
