package ru.example.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.taskservice.entity.Task;
import ru.example.taskservice.dto.TaskStatus;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

          List<Task> findAll();

          List<Task> getTasksByStatusAndUserId(TaskStatus status, Long userId);
}
