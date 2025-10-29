package ru.example.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class TaskDto {
          private Long id;
          private String title;
          private String owner;

          public TaskDto(Long id, String owner, String title) {
                    this.id = id;
                    this.owner = owner;
                    this.title = title;
          }

          public Long getId() {
                    return id;
          }

          public void setId(Long id) {
                    this.id = id;
          }

          public String getOwner() {
                    return owner;
          }

          public void setOwner(String owner) {
                    this.owner = owner;
          }

          public String getTitle() {
                    return title;
          }

          public void setTitle(String title) {
                    this.title = title;
          }
}