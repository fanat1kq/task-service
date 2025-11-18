package ru.example.taskservice.service.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static ru.example.taskservice.util.Constants.DLT_SUFFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CompletableFuture<SendResult<String, Object>> send(String topic, Object message) {
        log.debug("Sending message to topic: {}, message: {}", topic, message);
        return kafkaTemplate.send(topic, UUID.randomUUID().toString(), message);
    }

    public void sendToDlq(String mainTopic, Object message) {
        String dlqTopic = mainTopic + DLT_SUFFIX;
        send(dlqTopic, message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send to DLQ topic: {}", dlqTopic, ex);
            } else {
                log.info("Message moved to DLQ: {}", dlqTopic);
            }
        });
    }
}