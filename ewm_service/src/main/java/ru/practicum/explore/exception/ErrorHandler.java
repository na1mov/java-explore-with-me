package ru.practicum.explore.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValidException(final MethodArgumentNotValidException e) {
        return ApiError.builder()
                .status("BAD_REQUEST")
                .reason(e.getMessage())
                .message(e.getCause())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMyValidationException(final MyValidationException e) {
        return ApiError.builder()
                .status("BAD_REQUEST")
                .reason(e.getMessage())
                .message(e.getCause())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleMyConflictException(final MyConflictException e) {
        return ApiError.builder()
                .status("CONFLICT")
                .reason(e.getMessage())
                .message(e.getCause())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityException(final DataIntegrityViolationException e) {
        return ApiError.builder()
                .status("CONFLICT")
                .reason(e.getMessage())
                .message(e.getCause())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return ApiError.builder()
                .status("NOT_FOUND")
                .reason(e.getMessage())
                .message(e.getCause())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}
