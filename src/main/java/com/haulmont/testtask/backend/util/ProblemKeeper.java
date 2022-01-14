package com.haulmont.testtask.backend.util;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.Set;

public final class ProblemKeeper<T> {
    private String[] problems;

    private ProblemKeeper(Set<ConstraintViolation<T>> constraintViolations) {
        this.problems = constraintViolations.stream().map(ConstraintViolation::getMessage).toArray(String[]::new);
    }

    private ProblemKeeper() {
    }

    public void setProblems(Set<ConstraintViolation<T>> constraintViolations) {
        this.problems = constraintViolations.stream().map(ConstraintViolation::getMessage).toArray(String[]::new);
    }

    public static <T> ProblemKeeper<T> of(Set<ConstraintViolation<T>> constraintViolations) {
        return new ProblemKeeper<>(constraintViolations);
    }

    public static <T> ProblemKeeper<T> provide() {
        return new ProblemKeeper<>();
    }

    @Override
    public String toString() {
        return "errors: " + Arrays.stream(problems).reduce("", (s, s2) -> s + ", " + s2) + ".";
    }
}