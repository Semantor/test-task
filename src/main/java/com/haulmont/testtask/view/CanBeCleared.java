package com.haulmont.testtask.view;

import com.vaadin.flow.component.button.Button;

public interface CanBeCleared {
    Button getClearButton();

    default void tuneClearButton(){
        Button button = getClearButton();
        button.setText(Constant.CLEAR_TEXT);
        button.addThemeVariants(Constant.CLEAR_STYLE);
        button.addClickListener(event -> clear());
    }

    void clear();
}
