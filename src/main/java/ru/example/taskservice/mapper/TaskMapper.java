package ru.example.taskservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.example.taskservice.dto.TaskDto;
import ru.example.taskservice.dto.TaskRequestDto;
import ru.example.taskservice.entity.Task;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TaskMapper {

         TaskDto toDto(Task task);

          @Mapping(target = "description", ignore = true)
          Task toEntity(TaskDto taskDto);
          @Mapping(target = "description", ignore = true)
          @Mapping(target = "id", ignore = true)
          Task toEntity(TaskRequestDto taskDto);
        List<TaskDto> toDtoList(List<Task> tasks);
}
