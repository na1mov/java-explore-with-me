package ru.practicum.explore.exception;

public class MyForbiddenException extends RuntimeException {
    public MyForbiddenException(final String message) {
        super(message);
    }
}
