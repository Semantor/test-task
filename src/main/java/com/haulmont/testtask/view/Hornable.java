package com.haulmont.testtask.view;

import com.vaadin.flow.component.notification.Notification;
import org.slf4j.Logger;

import static com.haulmont.testtask.settings.ComponentSettings.DEFAULT_POSITION;
import static com.haulmont.testtask.settings.ComponentSettings.NOTIFICATION_DURATION;
import static com.haulmont.testtask.settings.Responses.LOG_DELIMITER;

public interface Hornable {
    String LOG_TEMPLATE_3 = "{}{}{}";
    String LOG_TEMPLATE_5 = "{}{}{}{}{}";
    String LOG_TEMPLATE_8 = "{}{}{}{}{}{}{}{}";

    Logger log();

    default void hornIntoNotificationAndLoggerInfo(String message, Object object) {
        Notification.show(message, NOTIFICATION_DURATION, DEFAULT_POSITION);
        log().info(LOG_TEMPLATE_3, message, LOG_DELIMITER, object);
    }

    default void hornIntoNotificationAndLoggerInfo(String message) {
        Notification.show(message, NOTIFICATION_DURATION, DEFAULT_POSITION);
        log().info(message);
    }
}
