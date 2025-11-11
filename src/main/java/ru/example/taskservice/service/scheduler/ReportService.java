package ru.example.taskservice.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.example.taskservice.client.AuthServiceClient;
import ru.example.taskservice.dto.EmailMessageRequestDto;
import ru.example.taskservice.dto.Notification;
import ru.example.taskservice.dto.NotificationType;
import ru.example.taskservice.dto.RecipientType;
import ru.example.taskservice.dto.TaskStatus;
import ru.example.taskservice.dto.TasksData;
import ru.example.taskservice.dto.UserDto;
import ru.example.taskservice.publisher.NotificationPublisher;
import ru.example.taskservice.service.TaskService;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

          private final AuthServiceClient authServiceClient;

          private final NotificationPublisher notificationPublisher;

          private final TaskService taskService;

          @Scheduled(cron = "0 0 22 * * *")
          public void sendDailyTasksSummaryToAllUsers() {
                    authServiceClient.getAllUsers().forEach(this::sendUserReport);
          }

          private void sendUserReport(UserDto user) {
                    var tasksData = getTasksData(user.getId());
                    var message = buildEmailMessage(user, tasksData);
                    notificationPublisher.send(message);
          }

          private TasksData getTasksData(Long userId) {
                    return new TasksData(
                              taskService.getTasksCountByStatusAndUserId(TaskStatus.COMPLETED,
                                        userId),
                              taskService.getTasksCountByStatusAndUserId(TaskStatus.NEW, userId),
                              taskService.getTasksCountByStatusAndUserId(TaskStatus.IN_PROGRESS,
                                        userId)
                    );
          }

          private Notification buildEmailMessage(UserDto user, TasksData tasks) {
                    return Notification.builder()
                              .to(user.getEmail())
                              .recipientType(RecipientType.EMAIL.getRecipientName())
                              .templateType(NotificationType.TASK_REPORT.getTemplateName())
                              .data(Map.of(
                                        "completedTasks", tasks.completed(),
                                        "inProgressTasks", tasks.inProgress(),
                                        "pendingCount", tasks.pending()
                              ))
                              .build();
          }
}