package ru.example.taskservice.dto.enumurates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    WELCOME("Welcome to Task Tracker", "WELCOME"),
    TASK_REPORT("Your daily task report", "TASK-SUMMARY");

    private final String defaultSubject;

    private final String templateName;


    public static NotificationType fromString(String type) {
        return Arrays.stream(values())
            .filter(typeFromEnum -> typeFromEnum.name().equals(type))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown Templatetype: " + type));
    }
}