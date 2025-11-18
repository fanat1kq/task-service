package ru.example.taskservice.dto.enumurates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    NEW("NEW_TASK"),
    IN_PROGRESS("IN_PROGRESS_TASK"),
    COMPLETED("COMPLETED_TASK");

    private final String taskStatus;
}
