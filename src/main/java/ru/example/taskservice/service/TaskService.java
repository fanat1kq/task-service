package ru.example.taskservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import ru.example.taskservice.dto.TaskDto;
import ru.example.taskservice.dto.TaskRequestDto;
import ru.example.taskservice.dto.TaskUpdateRequest;
import ru.example.taskservice.dto.UserInformationDto;
import ru.example.taskservice.dto.enumurates.TaskStatus;
import ru.example.taskservice.entity.Task;
import ru.example.taskservice.exception.TaskNotFoundException;
import ru.example.taskservice.mapper.TaskMapper;
import ru.example.taskservice.repository.TaskRepository;
import ru.example.taskservice.service.publisher.DataPublisher;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final DataPublisher dataPublisher;

    @Transactional(readOnly = true)
    public List<TaskDto> getTasks() {
        return taskMapper.toDtoList(taskRepository.findAll());
    }

    public void createTask(TaskRequestDto taskDto) {
        taskDto.setStatus(TaskStatus.NEW);
        taskRepository.save(taskMapper.toEntity(taskDto));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void updateTask(Long taskId, TaskUpdateRequest updateRequest) {
        Task task = taskRepository.findById(taskId).orElseThrow(
            () -> new TaskNotFoundException(taskId));

        task.setStatus(updateRequest.getStatus());
        task.setDescription(updateRequest.getDescription());
        task.setTitle(updateRequest.getTitle());
    }

    @Transactional(readOnly = true)
    public String getTasksCountByStatusAndUserId(TaskStatus status, Long userId) {
        return String.valueOf(
            taskRepository.getTasksByStatusAndUserId(status, userId).size()
        );
    }

    @PostMapping("/task/info")
    public void getAllTasksInfo(Long userId) {
        var dto = new UserInformationDto(userId);
        dataPublisher.sendUserInfo(dto);
    }
}
