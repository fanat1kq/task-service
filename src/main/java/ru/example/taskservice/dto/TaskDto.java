package ru.example.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.example.taskservice.entity.TaskStatus;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
          private Long id;
          private String title;
          private Long userId;
          private String description;
          private TaskStatus status;
}