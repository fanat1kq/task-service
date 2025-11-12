package ru.example.taskservice.service.publisher;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.taskservice.config.properties.RetryProperties;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryService {

          private final Cache<Long, AtomicInteger> retryCounters;
          private final RetryProperties retryProperties;

          // Существующие методы остаются
          public boolean shouldRetry(Long id) {
                    AtomicInteger counter = retryCounters.get(id, k -> new AtomicInteger(0));
                    int retryCount = counter.incrementAndGet();
                    return retryCount <= retryProperties.maxAttempts();
          }

          public int getCurrentRetryCount(Long id) {
                    AtomicInteger counter = retryCounters.getIfPresent(id);
                    return counter != null ? counter.get() : 0;
          }

          public void resetRetryCount(Long id) {
                    retryCounters.invalidate(id);
          }

          public Duration calculateBackoffDelay(int retryCount) {
                    long delaySeconds = (long) (retryProperties.initialDelay().getSeconds() *
                              Math.pow(retryProperties.multiplier(), retryCount - 1));
                    long maxDelaySeconds = retryProperties.maxDelay().getSeconds();
                    return Duration.ofSeconds(Math.min(delaySeconds, maxDelaySeconds));
          }

          public void markAsFailed(Long id) {
                    retryCounters.invalidate(id);
          }
}