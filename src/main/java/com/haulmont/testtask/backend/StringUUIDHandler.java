package com.haulmont.testtask.backend;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class StringUUIDHandler {

    /**
     * check for null, empty and correct
     *
     * @return UUID or null on fail validation
     */
    public @Nullable UUID validateStringUUIDAndReturnNullOrUUID(String uuidString) {
        if (uuidString == null) {
            log.warn("Nullable UUID");
            return null;
        }
        UUID fromString;
        try {
            fromString = UUID.fromString(uuidString);
        } catch (IllegalArgumentException ex) {
            log.warn("wrong UUID: " + uuidString);
            return null;
        }
        return fromString;
    }
}
