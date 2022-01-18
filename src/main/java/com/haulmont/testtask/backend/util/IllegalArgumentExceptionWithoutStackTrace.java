package com.haulmont.testtask.backend.util;

public class IllegalArgumentExceptionWithoutStackTrace extends RuntimeException {

    public IllegalArgumentExceptionWithoutStackTrace(String message) {
        super(message, null, false, false);
    }
}
