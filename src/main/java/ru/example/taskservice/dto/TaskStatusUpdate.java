package ru.example.taskservice.dto;

import ru.example.taskservice.entity.TaskStatus;

public class TaskStatusUpdate {
          private TaskStatus status;

          // геттер и сеттер
          public TaskStatus getStatus() {
                    return status;
          }

          public void setStatus(TaskStatus status) {
                    this.status = status;
          }
}