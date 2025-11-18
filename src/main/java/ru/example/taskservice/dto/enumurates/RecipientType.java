package ru.example.taskservice.dto.enumurates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipientType {

    EMAIL("EMAIL");

    private final String recipientName;
}
