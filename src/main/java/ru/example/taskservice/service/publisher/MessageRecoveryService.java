package ru.example.taskservice.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.taskservice.config.properties.RecoveryProperties;
import ru.example.taskservice.dto.Notification;
import ru.example.taskservice.dto.UserInformationDto;
import ru.example.taskservice.entity.FailedMessage;
import ru.example.taskservice.entity.enumurates.MessageStatus;
import ru.example.taskservice.repository.FailedMessageRepository;

import java.util.List;

import static ru.example.taskservice.util.Constants.LAST_ATTEMPT;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageRecoveryService {

    private final FailedMessageRepository failedMessageRepository;

    private final ObjectMapper objectMapper;

    private final DataPublisher dataPublisher;

    private final RecoveryProperties properties;

    @Value("${kafka.topics.email-sending}")
    private String emailSendingTopic;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void recoverFailedMessagesOnStartup() {
        if (!properties.isEnabled()) {
            log.info("Message recovery is disabled");
            return;
        }
        log.info("Recovering failed messages on application startup...");

        int page = 0;
        List<FailedMessage> failedMessages;

        do {
            Pageable pageable = PageRequest.of(page, properties.getBatchSize(),
                Sort.by(LAST_ATTEMPT));
            failedMessages =
                failedMessageRepository.findByStatus(MessageStatus.RETRYING,
                    pageable);

            log.info("Recovering batch {} with {} messages", page,
                failedMessages.size());
            failedMessages.forEach(this::recoverMessage);

            page++;
        } while (!failedMessages.isEmpty());

        log.info("Message recovery completed");
    }

    @Async
    @Transactional
    public void recoverMessage(FailedMessage failed) {
        try {
            Object message = deserializeMessage(failed.getMessage(),
                getMessageType(failed.getTopic()));
            switch (message) {
                case UserInformationDto userDto -> dataPublisher.sendUserInfo(userDto);
                case Notification notification -> dataPublisher.send(notification);
                case null ->
                    log.warn("Deserialized message is null for failed message: {}", failed.getId());
                default -> log.warn("Unknown message type for failed message: {}", failed.getId());
            }
            log.debug("Recovered message: {}", failed.getId());
        } catch (Exception e) {
            log.error("Failed to recover message: {}", failed.getId(), e);
        }
    }

    private Class<?> getMessageType(String topic) {
        if (topic.contains(emailSendingTopic)) return Notification.class;
        return Object.class;
    }

    private <T> T deserializeMessage(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message for type: {}",
                type.getSimpleName(), e);
            return null;
        }
    }
}