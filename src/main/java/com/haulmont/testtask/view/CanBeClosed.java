package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;

public interface CanBeClosed{
    Button getCloseButton();

    default void tuneCloseButton() {
        Button closeButton = getCloseButton();
        closeButton.addClickShortcut(Key.ESCAPE);
        closeButton.setText(Setting.CLOSE_BUTTON_TEXT);
        closeButton.addThemeVariants(Setting.CLOSE_STYLE);
        closeButton.addClickListener(event -> close());
    }

    void close();
}
