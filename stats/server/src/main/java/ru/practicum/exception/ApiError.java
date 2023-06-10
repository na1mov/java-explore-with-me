package ru.practicum.exception;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiError {
    String status;
    String reason;
    Throwable message;
    String timestamp;
}
