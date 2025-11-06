package ru.example.taskservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaTopicsProperties {
          private Topics topics = new Topics();

          @Data
          public static class Topics {
                    private String emailSending;
                    private String userInfo;
                    private String taskNotification;
          }
}