package ru.example.taskservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.taskservice.entity.FailedMessage;
import ru.example.taskservice.entity.MessageStatus;

import java.util.List;

public interface FailedMessageRepository extends JpaRepository<FailedMessage, Long> {
          List<FailedMessage> findByStatus(MessageStatus status, Pageable pageable);
}
