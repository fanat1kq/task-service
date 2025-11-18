package ru.example.taskservice.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.taskservice.entity.FailedMessage;
import ru.example.taskservice.entity.enumurates.MessageStatus;
import ru.example.taskservice.repository.FailedMessageRepository;

import java.time.LocalDateTime;

import static ru.example.taskservice.util.Constants.INITIAL_RETRY_COUNT;

@Slf4j
@Service
@RequiredArgsConstructor
public class FailedMessageService {

    private final FailedMessageRepository failedMessageRepository;

    private final ObjectMapper objectMapper;

    @Transactional
    public <T> void saveMessage(Long id, String topic, T message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            saveFailedMessage(id, topic, jsonMessage);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message with id: {}", id, e);
            saveMessageAsString(id, topic, message);
        }
    }

    private void saveFailedMessage(Long id, String topic, String messageContent) {
        FailedMessage failedMessage = buildFailedMessage(id, topic, messageContent);
        failedMessageRepository.save(failedMessage);
    }

    private <T> void saveMessageAsString(Long id, String topic, T message) {
        String fallbackContent = message.toString();
        saveFailedMessage(id, topic, fallbackContent);
    }

    private FailedMessage buildFailedMessage(Long id, String topic, String messageContent) {
        return FailedMessage.builder()
            .id(id)
            .topic(topic)
            .message(messageContent)
            .retryCount(INITIAL_RETRY_COUNT)
            .status(MessageStatus.RETRYING)
            .lastAttempt(LocalDateTime.now())
            .build();
    }
}