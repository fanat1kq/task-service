package ru.example.taskservice.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.example.taskservice.config.properties.RetryProperties;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Configuration
public class CacheConfig {

          @Bean
          public Cache<Long, AtomicInteger> retryCountersCache(RetryProperties retryProperties) {
                    return Caffeine.newBuilder()
                              .expireAfterWrite(retryProperties.cacheExpireAfterWrite())
                              .maximumSize(retryProperties.cacheMaximumSize())
                              .evictionListener((key, value, cause) ->
                                        log.debug("Evicted retry counter for user: {}, cause: {}", key, cause))
                              .build();
          }
}