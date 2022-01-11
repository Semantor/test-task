package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.vaadin.flow.component.button.Button;

public interface CanBeCleared {
    Button getClearButton();

    default void tuneClearButton(){
        Button button = getClearButton();
        button.setText(Setting.CLEAR_BUTTON_TEXT);
        button.addThemeVariants(Setting.CLEAR_STYLE);
        button.addClickListener(event -> clear());
    }

    void clear();
}
