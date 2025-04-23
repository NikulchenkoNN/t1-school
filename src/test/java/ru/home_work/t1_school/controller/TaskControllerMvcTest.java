package ru.home_work.t1_school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.home_work.t1_school.converter.TaskConverter;
import ru.home_work.t1_school.converter.TaskListConverter;
import ru.home_work.t1_school.kafka.KafkaMessageConsumer;
import ru.home_work.t1_school.kafka.KafkaMessageProducer;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.model.TaskDto;
import ru.home_work.t1_school.repository.TaskRepository;
import ru.home_work.t1_school.service.NotificationService;
import ru.home_work.t1_school.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private TaskRepository repository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskConverter taskConverter;

    @MockitoBean
    private KafkaMessageProducer kafkaMessageProducer;

    @MockitoBean
    private KafkaMessageConsumer kafkaMessageConsumer;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private TaskListConverter taskListConverter;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
    @Test
    void createTaskOk() throws Exception {
        TaskDto taskDto = createTaskDto();
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(taskDto.getUserId()))
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDto.getDescription()))
                .andExpect(jsonPath("$.state").value("CREATED"));
    }

    @Test
    void createTaskFailWithSetId() throws Exception {
        TaskDto taskDto = createTaskDto();
        taskDto.setId(1L);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(taskDto)))
                .andExpect(status().is(400));
    }

    @Test
    void getTaskOk() throws Exception {
        Task task = createTask();
        Task saved = taskService.create(task);
        mockMvc.perform(get("/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(task.getUserId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.state").value(task.getState()))
                .andExpect(jsonPath("$.id").value(task.getId()));
    }

    @Test
    void getTask404() throws Exception {
        mockMvc.perform(get("/tasks/100500"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTaskOk() throws Exception {
        Task task = createTask();
        Task saved = taskService.create(task);
        MvcResult mvcResult = mockMvc.perform(put("/tasks/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(taskConverter.convertToDto(task))))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("done");
        assertEquals(mvcResult.getResponse().getContentAsString(), "Задание " + saved.getId() + " обновленно");
        verify(kafkaMessageProducer).sendTo(anyString(), any());
    }

    @Test
    void updateTask404() throws Exception {
        TaskDto taskDto = createTaskDto();
        mockMvc.perform(put("/tasks/100500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(taskDto)))
                .andExpect(status().isNotFound());
        verify(kafkaMessageProducer, never()).sendTo(anyString(), any());
    }

    @Test
    void deleteTaskOk() throws Exception {
        Task task = taskService.create(createTask());
        MvcResult mvcResult = mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(mvcResult.getResponse().getContentAsString(), "Задание " + task.getId() + " удалено");
    }

    @Test
    void deleteTaskNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/tasks/100500"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void listTask() throws Exception {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task task = taskService.create(createTask());
            tasks.add(task);
        }
        MvcResult mvcResult = mockMvc.perform(get("/tasks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        List<TaskDto> responseDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<TaskDto>>() {
                });
        assertEquals(responseDtos.size(), tasks.size());
        List<Task> converted = taskListConverter.converToTaskList(responseDtos);
        assertEquals(converted, tasks);
    }

    private TaskDto createTaskDto() {
        TaskDto dto = new TaskDto();
        dto.setUserId(1L);
        dto.setTitle("title");
        dto.setDescription("description");
        return dto;
    }

    private Task createTask() {
        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("Description");
        task.setUserId(new Random().nextLong());
        return task;
    }
}
