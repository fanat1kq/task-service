package ru.example.taskservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskWebController {

          @GetMapping("/tasks")
          public String tasksPage() {
                    return "hello"; // Вернет HTML страницу
          }
}