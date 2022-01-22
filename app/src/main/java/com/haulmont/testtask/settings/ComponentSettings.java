package com.haulmont.testtask.settings;

import com.vaadin.flow.component.notification.Notification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComponentSettings {
    public static final int NOTIFICATION_DURATION = 4000;
    public static final Notification.Position DEFAULT_POSITION = Notification.Position.BOTTOM_CENTER;
    public static final String CLIENT_GRID_LAYOUT_SIZE = "500px";
    public static final String CREDIT_OFFER_GRID_LAYOUT_HEIGHT = "500px";
    public static final String ADD_NEW_CREDIT_OFFER_BUTTON_WIDTH = "10%";
    public static final String ENTITY_DESCRIPTION_MIN_WIDTH = "600px";
    public static final String DELETE_FORM_DIALOG_WIDTH = "40%";

    public static final boolean IS_SEARCH_FIELD_AUTOSELECTED = true;
    public static final boolean IS_SEARCH_FIELD_AUTOFOCUSED = true;
    public static final boolean IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT = true;
    public static final boolean IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT = true;
    public static final boolean IS_CREATE_CLIENT_FORM_DIALOG_CLOSE_ON_OUTSIDE_CLICK = false;

    public static final String PASSPORT_DELIMITER = " / ";
    public static final int NAME_FIELD_MIN_LENGTH = 3;
    public static final int PASSPORT_SERIES_START_INDEX = 0;
    public static final int PASSPORT_NUMBER_START_INDEX = 4;
    public static final int PASSPORT_SERIES_YEAR_START_POSITION = 2;
    public static final int PASSPORT_SERIES_MAX_VALUE = 9999;
    public static final int PASSPORT_SERIES_MIN_VALUE = 1000;
    public static final int PASSPORT_NUMBER_MIN_VALUE = 100000;
    public static final int PASSPORT_NUMBER_MAX_VALUE = 999999;
}
