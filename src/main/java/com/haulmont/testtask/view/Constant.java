package com.haulmont.testtask.view;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;

public class Constant {

    //todo take away to spring properties
    public static final ButtonVariant SAVE_STYLE = ButtonVariant.LUMO_PRIMARY;
    public static final ButtonVariant CLEAR_STYLE = ButtonVariant.LUMO_ERROR;
    public static final ButtonVariant CLOSE_STYLE = ButtonVariant.MATERIAL_CONTAINED;
    public static final ButtonVariant CALCULATE_STYLE = ButtonVariant.LUMO_PRIMARY;
    public static final ButtonVariant EDIT_STYLE = ButtonVariant.LUMO_LARGE;
    public static final ButtonVariant DELETE_STYLE = ButtonVariant.LUMO_LARGE;

    public static final String CLOSE_TEXT = "close";
    public static final String SAVE_TEXT = "save";
    public static final String CLEAR_TEXT = "clear";
    public static final String CALCULATE_TEXT = "calculate";
    public static final String EDIT_TEXT_BUTTON = "edit";
    public static final String ACCEPT_TEXT_BUTTON = "accept";
    public static final String DELETE_TEXT_BUTTON = "delete";

    public static final String DELETE_TEXT = "Are you really want to delete this?";


    public static final String CLIENT_GRID_LAYOUT_SIZE ="500px";

    public static final int NOTIFICATION_DURATION = 4000;
    public static final Notification.Position DEFAULT_POSITION = Notification.Position.BOTTOM_CENTER;

}
