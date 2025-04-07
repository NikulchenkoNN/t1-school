package ru.home_work.t1_school.converter;

import org.mapstruct.Mapper;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.model.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskConverter.class)
public interface TaskListConverter {
    List<TaskDto> converToDtoList(List<Task> taskList);
    List<Task> converToTaskList(List<TaskDto> taskDtoList);
}
