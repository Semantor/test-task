package com.haulmont.testtask.backend.excs;

public class DeleteException extends RuntimeException {

    /**
     * using {@link Throwable#Throwable(String, Throwable, boolean, boolean)}
     * to turn off {@link Throwable#fillInStackTrace()} excludes redundant stacktrace and make faster.
     *
     */
    public DeleteException(String message) {
        super(message, null, false, false);
    }
}
