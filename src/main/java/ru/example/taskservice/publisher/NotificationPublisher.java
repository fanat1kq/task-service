package ru.example.taskservice.publisher;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.example.taskservice.config.KafkaTopicsProperties;
import ru.example.taskservice.dto.EmailMessageRequestDto;
import ru.example.taskservice.dto.UserInformationDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {

          private final KafkaTemplate<String, Object> kafkaTemplate;

          private final KafkaTopicsProperties kafkaTopicsProperties;

          public void send(UserInformationDto dto) {
                    log.info("UserInformationDto: Sending message to consumer = {}", dto);
                    kafkaTemplate.send(kafkaTopicsProperties.getTopics().getUserInfo(), dto);
          }

          @SneakyThrows
          public void send(EmailMessageRequestDto dto) {
                    Message<EmailMessageRequestDto> emailMessage = MessageBuilder
                              .withPayload(dto)
                              .setHeader(KafkaHeaders.TOPIC, kafkaTopicsProperties.getTopics().getEmailSending())
                              .build();
                    log.info("EmailMessageRequestDto: Sending message to consumer = {}", dto);
                    kafkaTemplate.send(emailMessage).get();
          }
}
