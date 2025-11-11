package ru.example.taskservice.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record Notification(
    String to,
    String templateType,
    String recipientType,
    Map<String, String> data
) {
}
