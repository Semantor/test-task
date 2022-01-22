package com.haulmont.testtask.backend.util;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Result<R> implements Serializable {
    private final Exception t;
    private final R r;
    private final boolean isSuccess;

    private Result(Exception t, R r, boolean isSuccess) {
        this.t = t;
        this.r = r;
        this.isSuccess = isSuccess;
    }

    private Result(Exception t) {
        this(t, null, false);
    }


    /**
     * Returns true if this instance represents a failed outcome. In this case isSuccess returns false.
     */
    public boolean isFailure() {
        return !isSuccess;
    }

    /**
     * Returns true if this instance represents a successful outcome. In this case isFailure returns false.
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Returns the encapsulated Throwable exception if this instance represents failure or null if it is success.
     * This function is a shorthand for fold(onSuccess = { null }, onFailure = { it }) (see fold).
     */
    public Exception exceptionOrNull() {
        return t;
    }

    /**
     * Returns the encapsulated value if this instance represents success or null if it is failure.
     */
    public R getOrNull() {
        return r;
    }

    /**
     * Returns an instance that encapsulates the given Throwable as failure.
     */
    public static <R> Result<R> failure(Exception t) {
        return new Result<>(t);
    }

    /**
     * Returns an instance that encapsulates the given value as successful value.
     */
    public static <R> Result<R> success(R r) {
        return new Result<>(null, r, true);
    }

    /**
     * Returns the result of onSuccess for the encapsulated value if this instance represents success
     * or the result of onFailure function for the encapsulated Throwable exception if it is failure.
     */
    public <T> T fold(Function<R, T> onSuccess, Function<Exception, T> onFailure) {
        if (isSuccess) {
            return onSuccess.apply(r);
        }
        return onFailure.apply(t);
    }

    /**
     * Returns the encapsulated value if this instance represents success
     * or the defaultValue if it is failure.
     */
    public R getOrDefault(R defaultValue) {
        if (isSuccess)
            return r;
        return defaultValue;
    }

    /**
     * Returns the encapsulated value if this instance represents success
     * or the result of onFailure function for the encapsulated Throwable exception if it is failure.
     */
    public R getOrElse(Function<Exception, R> onFailure) {
        if (!isSuccess)
            return onFailure.apply(t);
        return r;
    }

    /**
     * Returns the encapsulated value if this instance represents success
     * or throws the encapsulated Throwable exception if it is failure.
     */
    public R getOrThrow(Exception exception) throws Exception {
        if (isSuccess)
            return r;
        throw exception;
    }

    /**
     * Returns the encapsulated result of the given transform function applied to the encapsulated value
     * if this instance represents success
     * or the original encapsulated Throwable exception if it is failure.
     */
    public <E> Result<E> map(Function<R, E> transform) throws Exception {
        if (isSuccess)
            return new Result<>(null, transform.apply(r), true);
        return (Result<E>) this;
    }

    /**
     * Returns the encapsulated result of the given transform function applied to the encapsulated value
     * if this instance represents success or the original encapsulated Throwable exception if it is failure.
     * <p>
     * This function catches any Throwable exception thrown by transform function and encapsulates it as a failure.
     * See map for an alternative that rethrows exceptions from transform function.
     */
    public <E> Result<E> mapCatching(Function<R, E> transform) {
        if (isSuccess) {
            try {
                return new Result<>(null, transform.apply(r), true);
            } catch (Exception exception) {
                return new Result<>(exception);
            }
        }
        return (Result<E>) this;
    }


    /**
     * Performs the given action on the encapsulated Throwable exception if this instance represents failure.
     * Returns the original Result unchanged.
     */
    public Result<R> onFailure(Consumer<Exception> doOnFailure) {
        if (!isSuccess)
            doOnFailure.accept(t);
        return this;
    }

    /**
     * Performs the given action on the encapsulated value if this instance represents success.
     * Returns the original Result unchanged.
     */
    public Result<R> onSuccess(Consumer<R> doOnSuccess) {
        if (isSuccess)
            doOnSuccess.accept(r);
        return this;
    }

    /**
     * Returns the encapsulated result of the given transform function applied
     * to the encapsulated Throwable exception if this instance represents failure
     * or the original encapsulated value if it is success.
     * <p>
     * Note, that this function rethrows any Throwable exception thrown by transform function.
     * See recoverCatching for an alternative that encapsulates exceptions.
     */
    public R recover(Function<Exception, R> recoverTransformation) throws Exception{
        if (!isSuccess)
            return recoverTransformation.apply(t);
        return r;
    }

    /**
     * Returns the encapsulated result of the given transform function applied
     * to the encapsulated Throwable exception if this instance represents failure
     * or the original encapsulated value if it is success.
     * <p>
     * This function catches any Throwable exception thrown by transform function and encapsulates it as a failure.
     * See recover for an alternative that rethrows exceptions.
     */
    public Result<R> recoverCatching(Function<Exception, R> recoverTransformation) {
        if (!isSuccess) {
            try {
                return new Result<>(null, recoverTransformation.apply(t), true);
            } catch (Exception exception) {
                return new Result<>(exception, null, false);
            }
        }
        return this;
    }

    /**
     * Returns a string Success(v) if this instance represents success
     * where v is a string representation of the value or a string Failure(x)
     * if it is failure where x is a string representation of the exception.
     */
    @Override
    public String toString() {
        return isSuccess ? r.toString() : t.toString();
    }

}
