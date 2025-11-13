package ru.example.taskservice.service.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.taskservice.config.properties.RetryProperties;
import ru.example.taskservice.entity.FailedMessage;
import ru.example.taskservice.entity.MessageStatus;
import ru.example.taskservice.repository.FailedMessageRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryService {

          private final FailedMessageRepository failedMessageRepository;

          private final FailedMessageService failedMessageService;

          private final RetryProperties retryProperties;

          public <T> boolean shouldRetry(Long id, String topic, T message) {
                    Optional<FailedMessage> existing = failedMessageRepository.findById(id);

                    if (existing.isEmpty()) {
                              failedMessageService.saveMessage(id, topic, message);
                              return true;
                    }

                    FailedMessage failedMessage = existing.get();
                    if (failedMessage.getRetryCount() >= retryProperties.maxAttempts()) {
                              log.warn("Max retries exceeded for message: {}", id);
                              return false;
                    }

                    failedMessage.setRetryCount(failedMessage.getRetryCount() + 1);
                    failedMessage.setLastAttempt(LocalDateTime.now());
                    failedMessageRepository.save(failedMessage);

                    return true;
          }

          public int getCurrentRetryCount(Long id) {
                    return failedMessageRepository.findById(id)
                              .map(FailedMessage::getRetryCount)
                              .orElse(0);
          }

          public void resetRetryCount(Long id) {
                    failedMessageRepository.deleteById(id);
          }

          public Duration calculateBackoffDelay(int retryCount) {
                    double delaySeconds = retryProperties.initialDelay().getSeconds() *
                              Math.pow(retryProperties.multiplier(), (double) retryCount - 1);
                    long maxDelaySeconds = retryProperties.maxDelay().getSeconds();
                    return Duration.ofSeconds((long) Math.min(delaySeconds, maxDelaySeconds));
          }

          public void markAsPermanentFailure(Long id) {
                    failedMessageRepository.findById(id).ifPresent(failedMessage -> {
                              failedMessage.setStatus(MessageStatus.PERMANENT_FAILURE);
                              failedMessageRepository.save(failedMessage);
                              log.warn("Marked message {} as permanent failure", id);
                    });
          }
}