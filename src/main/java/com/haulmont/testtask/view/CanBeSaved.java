package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;

public interface CanBeSaved {
    Button getSaveButton();

    default  void tuneSaveButton(){
        Button save = getSaveButton();
        save.addClickShortcut(Key.ENTER);
        save.setText(Setting.SAVE_BUTTON_TEXT);
        save.addThemeVariants(Setting.SAVE_STYLE);
        save.addClickListener(event -> validateAndSave());
    }

    void validateAndSave();
}
