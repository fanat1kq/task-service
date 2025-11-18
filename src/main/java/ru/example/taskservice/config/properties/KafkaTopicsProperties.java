package ru.example.taskservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaTopicsProperties {

    private Topics topics = new Topics();

    @Data
    public static class Topics {
        private String emailSending;

        private String userInfo;

        private String taskNotification;
    }
}