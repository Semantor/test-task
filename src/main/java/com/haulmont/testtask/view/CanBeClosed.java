package com.haulmont.testtask.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;

public interface CanBeClosed{
    Button getCloseButton();

    default void tuneCloseButton() {
        Button closeButton = getCloseButton();
        closeButton.addClickShortcut(Key.ESCAPE);
        closeButton.setText(Constant.CLOSE_TEXT);
        closeButton.addThemeVariants(Constant.CLOSE_STYLE);
        closeButton.addClickListener(event -> close());
    }

    void close();
}
