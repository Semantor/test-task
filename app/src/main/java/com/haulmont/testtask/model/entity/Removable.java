package com.haulmont.testtask.model.entity;

public interface Removable {
    default String toDeleteString() {
        return toString();
    }
}
