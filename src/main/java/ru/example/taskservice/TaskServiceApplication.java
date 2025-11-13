package ru.example.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.example.taskservice.config.properties.RecoveryProperties;
import ru.example.taskservice.config.properties.RetryProperties;
import ru.example.taskservice.service.scheduler.ReportService;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties({RetryProperties.class, RecoveryProperties.class})
@EnableFeignClients(basePackages = "ru.example")
public class TaskServiceApplication {

          public static void main(String[] args) {

                    ConfigurableApplicationContext run =
                              SpringApplication.run(TaskServiceApplication.class, args);
                    run.getBean(ReportService.class).sendDailyTasksSummaryToAllUsers();

          }

}
