package com.gaz.demo.bot.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TelegramSendException.class)
    public ResponseEntity<ApiError> handleTelegramError(TelegramSendException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ApiError("telegram_error", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralError(Exception ex) {
        log.error("Непредвиденная ошибка: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("server_error", "Что-то пошло не так. Попробуйте позже."));
    }
}
