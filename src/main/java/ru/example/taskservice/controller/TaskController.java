package ru.example.taskservice.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.taskservice.dto.TaskDto;
import ru.example.taskservice.dto.TaskRequestDto;
import ru.example.taskservice.dto.TaskStatusUpdate;
import ru.example.taskservice.dto.TaskUpdateRequest;
import ru.example.taskservice.entity.TaskStatus;
import ru.example.taskservice.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

          private final TaskService taskService;

          @GetMapping
          public List<TaskDto> getTasks() {
                    // Gateway уже проверил JWT, можно доверять запросу
                    return taskService.getTasks();
          }

          @PostMapping
          public void createTask(@RequestBody TaskRequestDto taskDto) {
                    taskService.createTask(taskDto);
          }

          @DeleteMapping("/{taskId}")
          public void deleteTask(@PathVariable Long taskId) {
                    taskService.deleteTask(taskId);
          }

          @PutMapping("/{taskId}")
          public void updateStatusById(@PathVariable Long taskId, @RequestBody
          TaskUpdateRequest updateRequest) {
                    taskService.updateTask(taskId, updateRequest);
          }
}

