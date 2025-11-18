package ru.example.taskservice.service.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.example.taskservice.config.properties.KafkaTopicsProperties;
import ru.example.taskservice.dto.Notification;
import ru.example.taskservice.dto.UserInformationDto;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataPublisher {

    private final PublisherService publisherService;

    private final RetryService retryService;

    private final ErrorHandlerService errorHandlerService;

    private final KafkaTopicsProperties kafkaTopicsProperties;

    public void sendUserInfo(UserInformationDto dto) {
        sendMessage(dto, kafkaTopicsProperties.getTopics().getUserInfo(), dto.id());
    }

    public void send(Notification dto) {
        Long messageId = generateNotificationId(dto);
        sendMessage(dto, kafkaTopicsProperties.getTopics().getEmailSending(), messageId);
    }

    private <T> void sendMessage(T dto, String topic, Long messageId) {
        log.info("Sending message to consumer = {}", dto);

        if (!retryService.shouldRetry(messageId, topic, dto)) {
            log.error("Max retries exceeded: {}, sending to DLQ immediately", messageId);
            errorHandlerService.handlePermanentFailure(topic, dto);
            return;
        }

        publisherService.send(topic, dto)
            .whenComplete((result, throwable) -> {
                if (throwable != null) {
                    handleSendFailure(topic, dto, throwable, messageId);
                } else {
                    handleSendSuccess(dto, result, messageId);
                }
            });
    }

    private <T> void handleSendSuccess(T dto, SendResult<String, Object> result,
                                       Long messageId) {
        log.info("Successfully sent: {} to topic: {}",
            getMessageIdentifier(dto),
            result.getRecordMetadata().topic());
        retryService.resetRetryCount(messageId);
    }

    private <T> void handleSendFailure(String topic, T dto, Throwable ex, Long messageId) {
        log.error("Failed to send: {}", getMessageIdentifier(dto), ex);

        int retryCount = retryService.getCurrentRetryCount(messageId);
        Duration delay = retryService.calculateBackoffDelay(retryCount);

        log.info("Scheduling retry {}: {} after {} ms",
            retryCount, messageId, delay.toMillis());

        scheduleRetry(() -> sendMessage(dto, topic, messageId), delay);
    }

    private <T> Object getMessageIdentifier(T dto) {
        if (dto instanceof UserInformationDto(Long id)) return id;
        return dto.toString();
    }

    private void scheduleRetry(Runnable retryTask, Duration delay) {
        errorHandlerService.scheduleRetry(retryTask, delay);
    }

    private Long generateNotificationId(Notification dto) {
        return (long) dto.hashCode();
    }
}