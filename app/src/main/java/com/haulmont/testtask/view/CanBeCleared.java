package com.haulmont.testtask.view;

import com.vaadin.flow.component.button.Button;

import static com.haulmont.testtask.settings.ButtonSettings.CLEAR_BUTTON_TEXT;
import static com.haulmont.testtask.settings.ButtonSettings.CLEAR_STYLE;

public interface CanBeCleared {
    Button getClearButton();

    default void tuneClearButton(){
        Button button = getClearButton();
        button.setText(CLEAR_BUTTON_TEXT);
        button.addThemeVariants(CLEAR_STYLE);
        button.addClickListener(event -> clear());
    }

    void clear();
}
