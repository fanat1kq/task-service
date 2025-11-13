package ru.example.taskservice.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.example.taskservice.entity.MessageStatus;
import ru.example.taskservice.repository.FailedMessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageRecoveryService {

          private final FailedMessageRepository failedMessageRepository;

          private final ObjectMapper objectMapper;

          private final DataPublisher dataPublisher;

          private final RecoveryProperties properties;

          @EventListener(ApplicationReadyEvent.class)
          @Transactional
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
                                        Sort.by("lastAttempt"));
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
                              if (message instanceof UserInformationDto userDto) {
                                        dataPublisher.sendUserInfo(userDto);
                              } else if (message instanceof Notification notification) {
                                        dataPublisher.send(notification);
                              }
                              log.debug("Recovered message: {}", failed.getId());
                    } catch (Exception e) {
                              log.error("Failed to recover message: {}", failed.getId(), e);
                              handleRecoveryFailure(failed, e);
                    }
          }

          private void handleRecoveryFailure(FailedMessage failed, Exception e) {
                    // Можно добавить логику для обработки неудачных восстановлений
                    log.warn("Recovery failed for message {}: {}", failed.getId(), e.getMessage());
          }

          private Class<?> getMessageType(String topic) {
                    if (topic.contains("user-info")) return UserInformationDto.class;
                    if (topic.contains("email")) return Notification.class;
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