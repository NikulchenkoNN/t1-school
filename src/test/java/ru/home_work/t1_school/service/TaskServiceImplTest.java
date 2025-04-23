package ru.home_work.t1_school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.home_work.t1_school.exception.TaskNotFoundException;
import ru.home_work.t1_school.kafka.KafkaMessageProducer;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.repository.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final KafkaMessageProducer kafkaProducer = mock(KafkaMessageProducer.class);
    TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository, kafkaProducer);
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
    @DisplayName("позитивный тест обновления задачи с отправкой в кафку")
    void updateTaskOkAndSendToKafka() {
        Task task = createTask();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        assertDoesNotThrow(() -> taskService.update(task.getId(), task));
        verify(kafkaProducer).sendTo(any(), any());
    }

    @Test
    @DisplayName("позитивный тест обновления задачи без отправки в кафку, без смены состояния")
    void updateTaskOkAndNotSendToKafka() {
        Task task = createTask();
        Task savedWitheStateNotUpdated = createTask();
        savedWitheStateNotUpdated.setState("FAILED");
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(savedWitheStateNotUpdated);

        assertDoesNotThrow(() -> taskService.update(task.getId(), task));
        verify(kafkaProducer, never()).sendTo(any(), any());
    }

    @Test
    void updateTaskThrowException() {
        Task task = createTask();
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.update(anyLong(), task));
        verify(kafkaProducer, never()).sendTo(any(), any());
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