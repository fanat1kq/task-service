package ru.example.taskservice.controller;



import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.taskservice.dto.TaskDto;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

          @GetMapping
          public List<TaskDto> getTasks() {
                    // Gateway уже проверил JWT, можно доверять запросу
                    return List.of(
                              new TaskDto(1L, "Task 1", "покормить кошку"),
                              new TaskDto(2L, "Task 2", "погладить собаку")
                    );
          }
}
