package ru.example.taskservice.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.taskservice.dto.ErrorResponse;
import ru.example.taskservice.exception.DlqSendException;
import ru.example.taskservice.exception.MessagePermanentFailureException;
import ru.example.taskservice.exception.MessageSendException;
import ru.example.taskservice.exception.MessageSerializationException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MessagePermanentFailureException.class)
    public ResponseEntity<ErrorResponse> handlePermanentFailure(
        MessagePermanentFailureException ex) {
        log.error("Permanent failure for message: {}", ex.getMessageId(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("MESSAGE_DELIVERY_FAILED",
                "Message permanently failed after retries", ex.getMessageId()));
    }

    @ExceptionHandler(MessageSerializationException.class)
    public ResponseEntity<ErrorResponse> handleSerializationError(
        MessageSerializationException ex) {
        log.error("Serialization error for message: {}", ex.getMessageId(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("MESSAGE_SERIALIZATION_ERROR",
                "Message serialization failed", ex.getMessageId()));
    }

    @ExceptionHandler(MessageSendException.class)
    public ResponseEntity<ErrorResponse> handleSendError(MessageSendException ex) {
        log.error("Send error for message: {}", ex.getMessageId(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ErrorResponse("MESSAGE_SEND_ERROR",
                "Failed to send message", ex.getMessageId()));
    }

    @ExceptionHandler(DlqSendException.class)
    public ResponseEntity<ErrorResponse> handleDlqError(DlqSendException ex) {
        log.error("DLQ send error for message: {}", ex.getMessageId(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("DLQ_SEND_ERROR",
                "Failed to send message to DLQ", ex.getMessageId()));
    }
}