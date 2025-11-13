package ru.example.taskservice.service.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import ru.example.taskservice.dto.Notification;
import ru.example.taskservice.dto.UserInformationDto;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErrorHandlerService {

          private final RetryService retryService;

          private final PublisherService publisherService;

          private final TaskScheduler taskScheduler;

          public void scheduleRetry(Runnable retryTask, Duration delay) {
                    taskScheduler.schedule(retryTask, Instant.now().plus(delay));
          }

          public void handlePermanentFailure(String mainTopic, Object dto, Throwable ex) {
                    Long messageId = extractMessageId(dto);
                    String type = extractMessageType(dto);

                    log.error("Max retries exceeded for {}: {}. Moving to DLQ", type, messageId);
                    retryService.markAsPermanentFailure(messageId);
                    publisherService.sendToDlq(mainTopic, dto);
                    log.warn("Message {} permanently failed and moved to DLQ", messageId);
          }

          private Long extractMessageId(Object dto) {
                    if (dto instanceof UserInformationDto userDto) return userDto.id();
                    return (long) dto.hashCode();
          }

          private String extractMessageType(Object dto) {
                    if (dto instanceof UserInformationDto) return "user";
                    if (dto instanceof Notification) return "notification";
                    return "unknown";
          }
}