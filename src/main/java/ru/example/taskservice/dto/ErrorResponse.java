package ru.example.taskservice.dto;

public record ErrorResponse(String code, String message, Long messageId) {
}