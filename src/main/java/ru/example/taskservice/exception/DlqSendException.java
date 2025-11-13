package ru.example.taskservice.exception;

import lombok.Getter;

@Getter
public class DlqSendException extends RuntimeException {
          private final Long messageId;

          public DlqSendException(Long messageId, String message) {
                    super(message);
                    this.messageId = messageId;
          }

          public DlqSendException(Long messageId, String message, Throwable cause) {
                    super(message, cause);
                    this.messageId = messageId;
          }
}