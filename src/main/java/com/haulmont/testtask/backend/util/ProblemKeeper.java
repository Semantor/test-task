package com.haulmont.testtask.backend.util;

import javax.validation.ConstraintViolation;
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
        if (problems==null || problems.length==0) return "No errors";
        StringBuilder stringBuilder = new StringBuilder("errors: ");
        for (String problem :
                problems) {
            stringBuilder.append(problem).append(",");
        }
        return stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), ".").toString();
    }
}