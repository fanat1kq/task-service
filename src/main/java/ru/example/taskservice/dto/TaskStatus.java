package ru.example.taskservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
          NEW("NEW"),
          IN_PROGRESS("IN_PROGRESS"),
          COMPLETED("COMPLETED");

          private final String taskStatus;
}
