package ru.home_work.t1_school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.home_work.t1_school.converter.TaskConverter;
import ru.home_work.t1_school.converter.TaskListConverter;
import ru.home_work.t1_school.exception.TaskNotFoundException;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.model.TaskDto;
import ru.home_work.t1_school.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TaskControllerTest {
    private final TaskService service = mock(TaskService.class);
    private final TaskConverter taskConverter = TaskConverter.INSTANCE;
    private final TaskListConverter listConverter = new TaskListConverter();
    private TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController(service, taskConverter, listConverter);
    }

    @Test
    void createTaskOk() {
        Task task = createTask();
        TaskDto dto = taskConverter.convertToDto(task);
        when(service.create(task)).thenReturn(task);
        TaskDto created = controller.createTask(dto);
        assertEquals(dto, created);
    }

    @Test
    void createTaskThrowsException() {
        Task task = createTask();
        TaskDto dto = taskConverter.convertToDto(task);
        when(service.create(task)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> controller.createTask(dto));
    }

    @Test
    void getTaskOk() {
        Task task = createTask();
        TaskDto dto = taskConverter.convertToDto(task);
        when(service.getTask(task.getId())).thenReturn(task);
        TaskDto extracted = controller.getTask(task.getId());
        assertEquals(dto, extracted);
    }

    @Test
    void getTaskThrowsException() {
        Task task = createTask();
        when(service.getTask(anyLong())).thenThrow(new TaskNotFoundException());
        assertThrows(TaskNotFoundException.class, () -> controller.getTask(task.getId()));
    }

    @Test
    void updateTaskOk() {
        Task task = createTask();
        TaskDto dto = taskConverter.convertToDto(task);
        assertDoesNotThrow(() -> controller.updateTask(task.getId(), dto));
    }

    @Test
    void updateTaskThrowsException() {
        Task task = createTask();
        TaskDto dto = taskConverter.convertToDto(task);
        doThrow(new TaskNotFoundException()).when(service).update(anyLong(), any(Task.class));
        assertThrows(TaskNotFoundException.class, () -> controller.updateTask(task.getId(), dto));
    }

    @Test
    void deleteTaskOk() {
        assertDoesNotThrow(() -> controller.deleteTask(anyLong()));
    }

    @Test
    void deleteTaskThrowsException() {
        doThrow(new TaskNotFoundException()).when(service).delete(anyLong());
        assertThrows(TaskNotFoundException.class, () -> controller.deleteTask(anyLong()));
    }

    @Test
    void getAllTasksOk() {
        ArrayList<Task> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(createTask());
        }
        when(service.list()).thenReturn(list);
        List<TaskDto> taskDtos = (List<TaskDto>) controller.listTasks();
        List<Task> extractedList = listConverter.converToTaskList(taskDtos);
        assertEquals(list, extractedList);
    }

    private Task createTask() {
        Task task = new Task();
        task.setId(new Random().nextLong());
        task.setTitle("Title");
        task.setDescription("Description");
        task.setUserId(new Random().nextLong());
        task.setState("state");
        return task;
    }

    private TaskDto createTaskDto() {
        TaskDto dto = new TaskDto();
        dto.setId(new Random().nextLong());
        dto.setTitle("Title");
        dto.setDescription("Description");
        dto.setUserId(new Random().nextLong());
        dto.setState("state");
        return dto;
    }
}