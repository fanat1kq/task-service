package ru.example.taskservice.service.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.example.taskservice.config.properties.KafkaTopicsProperties;
import ru.example.taskservice.dto.Notification;
import ru.example.taskservice.dto.UserInformationDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataPublisher {

          private final PublisherService publisherService;
          private final RetryService retryService;
          private final ErrorHandlerService errorHandlerService;
          private final KafkaTopicsProperties kafkaTopicsProperties;

          // Для UserInformationDto (без изменений)
          public void sendUserInfo(UserInformationDto dto) {
                    log.info("UserInformationDto: Sending message to consumer = {}", dto);
                    String topic = kafkaTopicsProperties.getTopics().getUserInfo();

                    publisherService.send(topic, dto)
                              .whenComplete((result, throwable) -> {
                                        if (throwable != null) {
                                                  handleUserInfoFailure(topic, dto, throwable);
                                        } else {
                                                  handleUserInfoSuccess(dto, result);
                                                  retryService.resetRetryCount(dto.id());
                                        }
                              });
          }

          // Для Notification с обработкой ошибок
          public void send(Notification dto) {
                    String topic = kafkaTopicsProperties.getTopics().getEmailSending();
                    log.info("Notification: Sending message to consumer = {}", dto);

                    publisherService.send(topic, dto)
                              .whenComplete((result, throwable) -> {
                                        if (throwable != null) {
                                                  handleNotificationFailure(topic, dto, throwable);
                                        } else {
                                                  handleNotificationSuccess(dto, result);

                                                  retryService.resetRetryCount(generateNotificationId(dto));
                                        }
                              });
          }

          private void handleUserInfoSuccess(UserInformationDto dto, SendResult<String, Object> result) {
                    log.info("Successfully sent user event: {} to topic: {}",
                              dto.id(), result.getRecordMetadata().topic());
          }

          private void handleUserInfoFailure(String topic, UserInformationDto dto, Throwable ex) {
                    log.error("Failed to send user event: {}", dto.id(), ex);

                    if (retryService.shouldRetry(dto.id())) {
                              int retryCount = retryService.getCurrentRetryCount(dto.id());
                              log.info("Scheduling retry {} for user: {}", retryCount, dto.id());
                              errorHandlerService.handleSendFailureAsync(dto, ex, retryCount)
                                        .thenRun(() -> sendUserInfo(dto));
                    } else {
                              errorHandlerService.handlePermanentFailure(topic, dto, ex);
                    }
          }

          private void handleNotificationSuccess(Notification dto, SendResult<String, Object> result) {
                    log.info("Successfully sent notification: {} to topic: {}",
                              dto, result.getRecordMetadata().topic());
          }

          private void handleNotificationFailure(String topic, Notification dto, Throwable ex) {
                    log.error("Failed to send notification: {}", dto, ex);

                    Long id = generateNotificationId(dto);
                    if (retryService.shouldRetry(id)) {
                              int retryCount = retryService.getCurrentRetryCount(id);
                              log.info("Scheduling retry {} for notification: {}", retryCount, id);
                              errorHandlerService.handleSendFailureAsync(dto, ex, retryCount)
                                        .thenRun(() -> send(dto));
                    } else {
                              errorHandlerService.handlePermanentFailure(topic, dto, ex);
                    }
          }

          private Long generateNotificationId(Notification dto) {
                    return (long) dto.hashCode();
          }
}