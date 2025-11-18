package ru.example.taskservice.dto;

import lombok.Data;
import ru.example.taskservice.dto.enumurates.TaskStatus;

@Data
public class TaskUpdateRequest {
    private String title;

    private String description;

    private TaskStatus status;
}