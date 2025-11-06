package ru.example.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.example.taskservice.entity.Task;
import ru.example.taskservice.entity.TaskStatus;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

          List<Task> findAll();


          List<Task> getTasksByStatusAndUserId(TaskStatus status, Long userId);
}
