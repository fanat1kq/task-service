package ru.example.taskservice.config.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "kafka.retry")
public record RetryProperties(
          @Min(1) int maxAttempts,
          @NotNull Duration initialDelay,
          @NotNull Duration maxDelay,
          double multiplier,
          @NotNull Duration counterTtl,
          @NotNull Duration cacheExpireAfterWrite,
          @Min(1) long cacheMaximumSize
) {
          public RetryProperties {
                    if (maxAttempts > 10) {
                              throw new IllegalArgumentException("Max attempts cannot exceed 10");
                    }
          }
}