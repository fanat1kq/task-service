package ru.example.taskservice.service.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.example.taskservice.dto.Notification;
import ru.example.taskservice.dto.UserInformationDto;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErrorHandlerService {

          private final RetryService retryService;
          private final PublisherService publisherService;

          // Для UserInformationDto
          @Async
          public CompletableFuture<Void> handleSendFailureAsync(UserInformationDto dto, Throwable ex, int retryCount) {
                    return handleSendFailureAsync(dto.id(), dto, ex, retryCount, "user");
          }

          public void handlePermanentFailure(String mainTopic, UserInformationDto dto, Throwable ex) {
                    handlePermanentFailure(mainTopic, dto, ex, dto.id(), "user");
          }

          // Для Notification
          @Async
          public CompletableFuture<Void> handleSendFailureAsync(Notification dto, Throwable ex, int retryCount) {
                    Long id = generateNotificationId(dto); // или dto.getId() если есть
                    return handleSendFailureAsync(id, dto, ex, retryCount, "notification");
          }

          public void handlePermanentFailure(String mainTopic, Notification dto, Throwable ex) {
                    Long id = generateNotificationId(dto); // или dto.getId() если есть
                    handlePermanentFailure(mainTopic, dto, ex, id, "notification");
          }

          // Общая логика
          @Async
          protected CompletableFuture<Void> handleSendFailureAsync(Long id, Object dto,
                                                                   Throwable ex, int retryCount,
                                                                   String type) {
                    try {
                              Duration delay = retryService.calculateBackoffDelay(retryCount);
                              log.info("Retrying {}: {} after {} seconds (attempt {})", type, id, delay.getSeconds(), retryCount);

                              sleepSafely(delay);
                              return CompletableFuture.completedFuture(null);

                    } catch (Exception e) {
                              log.error("Retry execution failed for {}: {}", type, id, e);
                              return CompletableFuture.failedFuture(e);
                    }
          }

          private void handlePermanentFailure(String mainTopic, Object dto, Throwable ex, Long id, String type) {
                    log.error("Max retries exceeded for {}: {}. Moving to DLQ", type, id);
                    publisherService.sendToDlq(mainTopic, dto);
                    retryService.markAsFailed(id);
          }

          private Long generateNotificationId(Notification dto) {
                    // Генерируем ID на основе содержимого или используем существующий
                    return (long) dto.hashCode(); // или dto.getId() если есть
          }

          private void sleepSafely(Duration delay) {
                    try {
                              Thread.sleep(delay.toMillis());
                    } catch (InterruptedException e) {
                              Thread.currentThread().interrupt();
                              throw new RuntimeException("Retry interrupted", e);
                    }
          }
}