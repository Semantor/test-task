package com.haulmont.testtask.settings;

import com.vaadin.flow.component.button.ButtonVariant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ButtonSettings {

    public static final ButtonVariant SAVE_STYLE = ButtonVariant.LUMO_PRIMARY;
    public static final ButtonVariant CLEAR_STYLE = ButtonVariant.LUMO_ERROR;
    public static final ButtonVariant CLOSE_STYLE = ButtonVariant.MATERIAL_CONTAINED;
    public static final ButtonVariant CALCULATE_STYLE = ButtonVariant.LUMO_PRIMARY;
    public static final ButtonVariant EDIT_STYLE = ButtonVariant.LUMO_LARGE;
    public static final ButtonVariant DELETE_STYLE = ButtonVariant.LUMO_LARGE;
    public static final ButtonVariant DETAILS_STYLE = ButtonVariant.LUMO_SMALL;

    public static final String CLOSE_BUTTON_TEXT = "close";
    public static final String SAVE_BUTTON_TEXT = "save";
    public static final String CLEAR_BUTTON_TEXT = "clear";
    public static final String CALCULATE_BUTTON_TEXT = "calculate";
    public static final String EDIT_BUTTON_TEXT = "edit";
    public static final String ACCEPT_BUTTON_TEXT = "accept";
    public static final String DELETE_BUTTON_TEXT = "delete";
    public static final String DETAILS_BUTTON_TEXT = "Details";
    public static final String NEXT_BUTTON_TEXT = "next";
    public static final String UPDATE_BUTTON_TEXT = "update";
    public static final String PREVIOUS_BUTTON_TEXT = "previous";
    public static final String CREATE_NEW_BUTTON_TEXT = "create new client";
    public static final String CREATE_NEW_BANK_BUTTON_TEXT = "create new bank";
    public static final String CREATE_NEW_CREDIT_BUTTON_TEXT = "create new credit";
    public static final String SHOW_CREDIT_LIST_BUTTON_TEXT = "show credit list";
    public static final String SHOW_BANK_LIST_BUTTON_TEXT = "show bank list";
    public static final String ADD_CREDIT_OFFER_BUTTON_TEXT = "add credit offer";
}
