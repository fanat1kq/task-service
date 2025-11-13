package ru.example.taskservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedMessage {

          @Id
          private Long id;

          @Column(nullable = false)
          private String topic;

          @Column(columnDefinition = "TEXT", nullable = false)
          private String message;

          @Column(nullable = false)
          private int retryCount = 0;

          @Column(nullable = false)
          @Enumerated(EnumType.STRING)
          private MessageStatus status = MessageStatus.RETRYING;

          @Column(nullable = false)
          private LocalDateTime lastAttempt = LocalDateTime.now();
}