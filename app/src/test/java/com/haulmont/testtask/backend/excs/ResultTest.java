package com.haulmont.testtask.backend.excs;

import com.haulmont.testtask.backend.util.Result;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private final String successString = "255";
    private final String failString = "322";
    private final Exception failException = new IllegalArgumentException(failString);
    private final Exception anotherException = new ArrayIndexOutOfBoundsException();
    private final Result<String> success = Result.success(successString);
    private final Result<String> failure = Result.failure(failException);
    private final int successInt = 255;
    private final int failureInt = 322;
    private final String defaultString = "defaultMsg";


    @Test
    void isFailure() {
        assertTrue(failure.isFailure());
        assertFalse(success.isFailure());
    }

    @Test
    void isSuccess() {
        assertTrue(success.isSuccess());
        assertFalse(failure.isSuccess());
    }

    @Test
    void exceptionOrNull() {
        assertNull(success.exceptionOrNull());
        assertEquals(failException, failure.exceptionOrNull());
    }

    @Test
    void getOrNull() {
        assertEquals(successString, success.getOrNull());
        assertNull(failure.getOrNull());
    }

    @Test
    void foldTest() {
        Function<String, Integer> onSuc = Integer::parseInt;
        Function<Exception, Integer> onFail = t -> Integer.parseInt(t.getMessage());
        assertEquals(success.fold(onSuc, onFail), successInt);
        assertEquals(failure.fold(onSuc, onFail), failureInt);
    }

    @Test
    void getOrDefaultTest() {
        assertEquals(successString, success.getOrDefault(defaultString));
        assertEquals(defaultString, failure.getOrDefault(defaultString));
    }

    @Test
    void getOrElseTest() {
        Function<Exception, String> onFailure = Throwable::getMessage;
        assertEquals(successString, success.getOrElse(onFailure));
        assertEquals(failString, failure.getOrElse(onFailure));
    }

    @Test
    void getOrThrowTest() throws Exception {
        assertEquals(successString, success.getOrThrow(anotherException));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> failure.getOrThrow(anotherException));
    }

    @Test
    void mapTest() throws Throwable {
        Function<String, Integer> transform = Integer::parseInt;
        Function<String, Byte> transformWithException = Byte::parseByte;
        assertEquals(successInt, success.map(transform).getOrNull());
        assertThrows(NumberFormatException.class, () -> success.map(transformWithException));
        assertEquals(failure, failure.map(transform));
    }

    @Test
    void mapCatchingTest() {
        Function<String, Integer> transform = Integer::parseInt;
        Function<String, Byte> transformWithException = Byte::parseByte;
        Result<Byte> mapResult = success.mapCatching(transformWithException);
        assertEquals(successInt, success.mapCatching(transform).getOrNull());
        assertTrue(mapResult.isFailure());
        assertEquals("Value out of range. Value:\"" + successString + "\" Radix:" + 10, mapResult.exceptionOrNull().getMessage());
        assertEquals(failure, failure.mapCatching(transformWithException));
    }

    @Test
    void toStringTest() {
        assertEquals(successString, success.toString());
        assertEquals(failException.toString(), failure.toString());
    }

    @Test
    void recoverTest() throws Throwable {
        Function<Exception, String> transform = Throwable::getMessage;
        Function<Exception, String> transformWithException = throwable -> Byte.parseByte(throwable.getMessage()) + "";
        assertEquals(failString, failure.recover(transform));
        assertEquals(successString, success.recover(transform));
        assertThrows(NumberFormatException.class, () -> failure.recover(transformWithException));

    }

    @Test
    void recoverCatching() {
        Function<Exception, String> transform = Throwable::getMessage;
        Function<Exception, String> transformWithException = throwable -> Byte.parseByte(throwable.getMessage()) + "";
        Result<String> recoverResult = failure.recoverCatching(transformWithException);
        assertEquals(success, success.recoverCatching(transform));
        assertEquals(failString, failure.recoverCatching(transform).getOrNull());
        assertTrue(recoverResult.isFailure());
        assertEquals("Value out of range. Value:\"" + failString + "\" Radix:" + 10, recoverResult.exceptionOrNull().getMessage());
    }

    @Test
    void onFailureTest() {
        AtomicInteger successAtomic = new AtomicInteger(0);
        AtomicInteger failureAtomic = new AtomicInteger(0);
        failure.onFailure(t->failureAtomic.addAndGet(1));
        success.onFailure(t->successAtomic.addAndGet(1));
        assertTrue(failureAtomic.get()>successAtomic.get());
    }

    @Test
    void onSuccessTest() {
        AtomicInteger successAtomic = new AtomicInteger(0);
        AtomicInteger failureAtomic = new AtomicInteger(0);
        failure.onSuccess(t->failureAtomic.addAndGet(1));
        success.onSuccess(t->successAtomic.addAndGet(1));
        assertTrue(failureAtomic.get()<successAtomic.get());
    }

}