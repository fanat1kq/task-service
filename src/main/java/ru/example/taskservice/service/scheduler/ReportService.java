package ru.example.taskservice.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.example.taskservice.client.AuthServiceClient;
import ru.example.taskservice.dto.EmailMessageRequestDto;
import ru.example.taskservice.dto.NotificationType;
import ru.example.taskservice.dto.RecipientType;
import ru.example.taskservice.dto.UserDto;
import ru.example.taskservice.entity.TaskStatus;
import ru.example.taskservice.publisher.NotificationPublisher;
import ru.example.taskservice.service.TaskService;

import java.util.List;
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


                    List<UserDto> allUsers = authServiceClient.getAllUsers();

                    for (UserDto userDto : allUsers) {

                              String completedTasks = taskService.getTasksCountByStatusAndUserId(
                                        TaskStatus.COMPLETED, userDto.getId());
                              String newTasks =
                                        taskService.getTasksCountByStatusAndUserId(TaskStatus.NEW,
                                                  userDto.getId());
                              String inProgressTasks = taskService.getTasksCountByStatusAndUserId(
                                        TaskStatus.IN_PROGRESS, userDto.getId());

                              EmailMessageRequestDto message = EmailMessageRequestDto
                                        .builder()
                                        .to(userDto.getEmail())
                                        .recipientType(RecipientType.EMAIL.getRecipientName())
                                        .templateType(
                                                  NotificationType.TASK_REPORT.getTemplateName())
                                        .data(Map.of("completedTasks", completedTasks,
                                                  "inProgressTasks", newTasks,
                                                  "pendingCount", inProgressTasks))
                                        .build();

                              notificationPublisher.send(message);
                    }
          }
}
