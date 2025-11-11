package ru.example.taskservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.example.taskservice.dto.UserDto;

import java.util.List;

@FeignClient(
          name = "auth-service",
          url = "${services.auth-service.url}"
)
public interface AuthServiceClient {

          @GetMapping("/api/auth/users")
          List<UserDto> getAllUsers();
}
