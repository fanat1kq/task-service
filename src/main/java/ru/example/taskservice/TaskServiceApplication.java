package ru.example.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import ru.example.taskservice.service.scheduler.ReportService;

@SpringBootApplication
@EnableFeignClients(basePackages = "ru.example")
public class TaskServiceApplication {

          public static void main(String[] args) {

                    ConfigurableApplicationContext run =
                              SpringApplication.run(TaskServiceApplication.class, args);
                    run.getBean(ReportService.class).sendDailyTasksSummaryToAllUsers();

          }

}
