package ru.practicum.explore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValidException(final MethodArgumentNotValidException e) {
        log.debug("Получен статус BAD_REQUEST {}", e.getMessage());
        return apiErrorBuilder(e, "BAD_REQUEST");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameter(final MissingServletRequestParameterException e) {
        log.debug("Получен статус BAD_REQUEST {}", e.getMessage());
        return apiErrorBuilder(e, "BAD_REQUEST");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMyValidationException(final MyValidationException e) {
        log.debug("Получен статус BAD_REQUEST {}", e.getMessage());
        return apiErrorBuilder(e, "BAD_REQUEST");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleMyConflictException(final MyConflictException e) {
        log.debug("Получен статус CONFLICT {}", e.getMessage());
        return apiErrorBuilder(e, "CONFLICT");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleMyForbiddenException(final MyForbiddenException e) {
        log.debug("Получен статус FORBIDDEN {}", e.getMessage());
        return apiErrorBuilder(e, "FORBIDDEN");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityException(final DataIntegrityViolationException e) {
        log.debug("Получен статус CONFLICT {}", e.getMessage());
        return apiErrorBuilder(e, "CONFLICT");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.debug("Получен статус NOT_FOUND {}", e.getMessage());
        return apiErrorBuilder(e, "NOT_FOUND");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowableException(final Throwable e) {
        log.debug("Получен статус INTERNAL_SERVER_ERROR {}", e.getMessage());
        return apiErrorBuilder(e, "INTERNAL_SERVER_ERROR");
    }

    private ApiError apiErrorBuilder(Throwable e, String status) {
        return ApiError.builder()
                .status(status)
                .reason(e.getMessage())
                .message(e.getCause())
                .build();
    }
}
