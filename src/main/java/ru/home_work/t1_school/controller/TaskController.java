package ru.home_work.t1_school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.home_work.t1_school.converter.TaskConverter;
import ru.home_work.t1_school.converter.TaskListConverter;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.model.TaskDto;
import ru.home_work.t1_school.service.TaskService;
import ru.home_work.t1_starter.aspect.annotations.LogMethodCallWithParams;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final TaskConverter taskConverter;
    private final TaskListConverter listConverter;

    @LogMethodCallWithParams
    @PostMapping(value = "/tasks")
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        Task task = taskConverter.convertToTask(taskDto);
        Task created = service.create(task);
        return taskConverter.convertToDto(created);
    }

    @LogMethodCallWithParams
    @GetMapping(value = "/tasks/{id}")
    public TaskDto getTask(@PathVariable("id") Long id) {
        return taskConverter.convertToDto(service.getTask(id));
    }

    @LogMethodCallWithParams
    @PutMapping(value = "/tasks/{id}")
    public String updateTask(@PathVariable("id") Long id,
                             @RequestBody TaskDto dto) {
        Task task = taskConverter.convertToTask(dto);
        service.update(id, task);
        return "Задание " + id + " обновленно";
    }

    @LogMethodCallWithParams
    @DeleteMapping(value = "/tasks/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        service.delete(id);
        return "Задание " + id + " удалено";
    }

    @LogMethodCallWithParams
    @GetMapping(value = "/tasks")
    public Collection<TaskDto> listTasks() {
        return listConverter.converToDtoList(service.list());
    }
}
