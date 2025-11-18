package ru.example.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.example.taskservice.dto.enumurates.TaskStatus;

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