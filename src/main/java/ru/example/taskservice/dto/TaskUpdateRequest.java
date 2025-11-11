package ru.example.taskservice.dto;

import lombok.Data;

@Data
public class TaskUpdateRequest {
          private String title;
          private String description;
          private TaskStatus status;
}