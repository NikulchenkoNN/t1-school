package ru.home_work.t1_school.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.model.TaskDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskListConverter {

    public List<TaskDto> converToDtoList(List<Task> taskList) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : taskList) {
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setDescription(task.getDescription());
            dto.setState(task.getState());
            dto.setTitle(task.getTitle());
            dto.setUserId(task.getUserId());
            taskDtoList.add(dto);
        }
        return taskDtoList;
    }

    public List<Task> converToTaskList(List<TaskDto> taskDtoList) {
        List<Task> taskList = new ArrayList<>();
        for (TaskDto dto : taskDtoList) {
            Task task = new Task();
            task.setId(dto.getId());
            task.setDescription(dto.getDescription());
            task.setState(dto.getState());
            task.setTitle(dto.getTitle());
            task.setUserId(dto.getUserId());
            taskList.add(task);
        }
        return taskList;
    }
}
