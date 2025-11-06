package ru.example.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "ru.example")
public class TaskServiceApplication {

          public static void main(String[] args) {

                    SpringApplication.run(TaskServiceApplication.class, args);

          }

}
