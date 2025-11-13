package ru.example.taskservice.exception;

public class MessageSendException extends RuntimeException {
          private final Long messageId;

          public MessageSendException(Long messageId, String message) {
                    super(message);
                    this.messageId = messageId;
          }

          public MessageSendException(Long messageId, String message, Throwable cause) {
                    super(message, cause);
                    this.messageId = messageId;
          }

          public Long getMessageId() {
                    return messageId;
          }
}