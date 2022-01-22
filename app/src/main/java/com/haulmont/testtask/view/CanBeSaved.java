package com.haulmont.testtask.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;

import static com.haulmont.testtask.settings.ButtonSettings.SAVE_BUTTON_TEXT;
import static com.haulmont.testtask.settings.ButtonSettings.SAVE_STYLE;

public interface CanBeSaved {
    Button getSaveButton();

    default void tuneSaveButton() {
        Button save = getSaveButton();
        save.addClickShortcut(Key.ENTER);
        save.setText(SAVE_BUTTON_TEXT);
        save.addThemeVariants(SAVE_STYLE);
        save.addClickListener(event -> validateAndSave());
    }

    void validateAndSave();
}
