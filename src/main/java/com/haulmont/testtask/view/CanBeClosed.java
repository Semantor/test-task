package com.haulmont.testtask.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;

import static com.haulmont.testtask.settings.ButtonSettings.CLOSE_BUTTON_TEXT;
import static com.haulmont.testtask.settings.ButtonSettings.CLOSE_STYLE;

public interface CanBeClosed{
    Button getCloseButton();

    default void tuneCloseButton() {
        Button closeButton = getCloseButton();
        closeButton.addClickShortcut(Key.ESCAPE);
        closeButton.setText(CLOSE_BUTTON_TEXT);
        closeButton.addThemeVariants(CLOSE_STYLE);
        closeButton.addClickListener(event -> close());
    }

    void close();
}
