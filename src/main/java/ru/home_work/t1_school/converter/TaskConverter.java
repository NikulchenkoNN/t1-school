package ru.home_work.t1_school.converter;

import org.mapstruct.Mapper;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.model.TaskDto;

@Mapper(componentModel = "spring")
public interface TaskConverter {
    TaskDto convertToDto(Task task);
    Task convertToTask(TaskDto taskDto);
}
