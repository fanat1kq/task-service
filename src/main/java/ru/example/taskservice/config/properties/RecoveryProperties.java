package ru.example.taskservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.message.recovery")
@Getter
@Setter
public class RecoveryProperties {

    private boolean enabled;

    private int batchSize;

    private Duration initialDelay;

    private int maxRecoveryAttempts;
}