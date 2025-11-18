package ru.example.taskservice.exception;

import lombok.Getter;

@Getter
public class MessagePermanentFailureException extends RuntimeException {
    private final Long messageId;

    private final String messageType;

    public MessagePermanentFailureException(Long messageId, String messageType,
                                            String message) {
        super(message);
        this.messageId = messageId;
        this.messageType = messageType;
    }

    public MessagePermanentFailureException(Long messageId, String messageType,
                                            String message, Throwable cause) {
        super(message, cause);
        this.messageId = messageId;
        this.messageType = messageType;
    }

    public MessagePermanentFailureException(String message) {
        super(message);
        this.messageId = null;
        this.messageType = "unknown";
    }
}