package ru.home_work.t1_school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.home_work.t1_school.exception.TaskNotFoundException;
import ru.home_work.t1_school.kafka.KafkaMessageProducer;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.repository.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final KafkaMessageProducer producer = mock(KafkaMessageProducer.class);
    TaskService taskService = new TaskServiceImpl(taskRepository, producer);

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository,producer);
    }

    @Test
    void createOk() {
        Task task = createTask();

        when(taskRepository.save(task)).thenReturn(task);
        Task createdTask = taskService.create(task);
        assertNotNull(createdTask);
        assertEquals(task, createdTask);
    }

    @Test
    void getTaskOk() {
        Task task = createTask();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Task asserted = assertDoesNotThrow(() -> taskService.getTask(task.getId()));
        assertEquals(task, taskService.getTask(asserted.getId()));
    }

    @Test
    void getTaskThrowException() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(anyLong()));
    }

    @Test
    void updateTaskOk() {
        Task task = createTask();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        assertDoesNotThrow(() -> taskService.update(task.getId(), task));
    }

    @Test
    void updateTaskThrowException() {
        Task task = createTask();
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.update(anyLong(), task));
    }

    @Test
    void deleteTaskOk() {
        Task task = createTask();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        assertDoesNotThrow(() -> taskService.delete(task.getId()));
    }

    @Test
    void deleteTaskThrowException() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.delete(anyLong()));
    }


    private Task createTask() {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("description");
        task.setTitle("title");
        task.setUserId(1L);
        return task;
    }
}