package ru.example.taskservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipientType {

          EMAIL("EMAIL");

          private final String recipientName;
}
