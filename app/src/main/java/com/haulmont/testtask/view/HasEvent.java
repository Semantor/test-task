package com.haulmont.testtask.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;

public interface HasEvent {

    ComponentEventBus getEventBusFromLayout();

    default <T extends ComponentEvent<?>> Registration addEventListener(Class<T> eventType,
                                                                        ComponentEventListener<T> listener) {
        return getEventBusFromLayout().addListener(eventType, listener);
    }
}
