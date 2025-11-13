package ru.example.taskservice.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.taskservice.entity.FailedMessage;
import ru.example.taskservice.entity.MessageStatus;
import ru.example.taskservice.repository.FailedMessageRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FailedMessageService {

          private final FailedMessageRepository failedMessageRepository;

          private final ObjectMapper objectMapper; // Spring Boot автоматически настраивает

          public <T> void saveMessage(Long id, String topic, T message) {
                    try {
                              String jsonMessage = objectMapper.writeValueAsString(message);

                              FailedMessage failedMessage = FailedMessage.builder()
                                        .id(id)
                                        .topic(topic)
                                        .message(jsonMessage)
                                        .retryCount(0)
                                        .status(MessageStatus.RETRYING)
                                        .lastAttempt(LocalDateTime.now())
                                        .build();

                              failedMessageRepository.save(failedMessage);
                              log.debug("Saved failed message with id: {}", id);

                    } catch (JsonProcessingException e) {
                              log.error("Failed to serialize message with id: {}", id, e);
                              // Fallback: сохраняем toString()
                              saveMessageAsString(id, topic, message);
                    }
          }

          private <T> void saveMessageAsString(Long id, String topic, T message) {
                    FailedMessage failedMessage = FailedMessage.builder()
                              .id(id)
                              .topic(topic)
                              .message(message.toString())
                              .retryCount(0)
                              .status(MessageStatus.RETRYING)
                              .lastAttempt(LocalDateTime.now())
                              .build();

                    failedMessageRepository.save(failedMessage);
          }
}