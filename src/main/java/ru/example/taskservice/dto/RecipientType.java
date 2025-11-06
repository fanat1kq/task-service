package ru.example.taskservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RecipientType {

          EMAIL("EMAIL");

          private final String recipientName;

          public static RecipientType fromString(String type) {
                    return Arrays.stream(values())
                              .filter(typeFromEnum -> typeFromEnum.name().equals(type))
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("Unknown email RecipientType: " + type));
          }
}
