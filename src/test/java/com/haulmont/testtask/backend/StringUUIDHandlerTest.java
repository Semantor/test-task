package com.haulmont.testtask.backend;

import com.haulmont.testtask.Config;
import com.haulmont.testtask.TestConfigForNonJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {Config.class, TestConfigForNonJpa.class})
class StringUUIDHandlerTest {
    @Autowired
    private StringUUIDHandler stringUUIDHandler;

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "asdfasdf"})
    void validateStringUUIDAndReturnNullOrUUID(String validatingString) {
        assertNull(stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(validatingString));
    }

    @Test
    void validateUUIDWithNullInput() {
        assertNull(stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(null));
    }

    @Test
    void validateUUIDWithCorrectInput() {
        String input = "c9457c4d-d778-4ef4-abd8-917212cddb75";
        UUID expected = UUID.fromString("c9457c4d-d778-4ef4-abd8-917212cddb75");
        assertEquals(expected, stringUUIDHandler.validateStringUUIDAndReturnNullOrUUID(input));
    }
}