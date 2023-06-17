package ru.practicum.explore.exception;

public class MyValidationException extends RuntimeException {
    public MyValidationException(final String message) {
        super(message);
    }
}
