package ru.example.taskservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
          NEW("NEW"),
          IN_PROGRESS("IN_PROGRESS"),
          COMPLETED("COMPLETED");

          private final String TaskStatus;
}
