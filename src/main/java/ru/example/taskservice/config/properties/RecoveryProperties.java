package ru.example.taskservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.message.recovery")
@Getter
@Setter
public class RecoveryProperties {
          private boolean enabled = true;
          private int batchSize = 50;
          private Duration initialDelay = Duration.ofSeconds(30);
          private int maxRecoveryAttempts = 3;
}