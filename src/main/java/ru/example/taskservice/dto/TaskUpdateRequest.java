package ru.example.taskservice.dto;

import lombok.Data;
import ru.example.taskservice.entity.TaskStatus;

@Data
public class TaskUpdateRequest {
          private String title;
          private String description;
          private TaskStatus status;
          // геттеры и сеттеры
}