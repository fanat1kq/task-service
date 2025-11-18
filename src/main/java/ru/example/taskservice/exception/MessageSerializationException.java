package ru.example.taskservice.exception;

import lombok.Getter;

@Getter
public class MessageSerializationException extends RuntimeException {
    private final Long messageId;

    public MessageSerializationException(Long messageId, String message) {
        super(message);
        this.messageId = messageId;
    }

    public MessageSerializationException(Long messageId, String message, Throwable cause) {
        super(message, cause);
        this.messageId = messageId;
    }
}