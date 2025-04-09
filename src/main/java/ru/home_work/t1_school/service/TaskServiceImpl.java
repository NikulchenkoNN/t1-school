package ru.home_work.t1_school.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.home_work.t1_school.aspect.annotations.LogException;
import ru.home_work.t1_school.aspect.annotations.LogExecutionTime;
import ru.home_work.t1_school.aspect.annotations.LogReturning;
import ru.home_work.t1_school.exception.TaskNotFoundException;
import ru.home_work.t1_school.kafka.KafkaMessageProducer;
import ru.home_work.t1_school.model.MessageDto;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final KafkaMessageProducer producer;
    private final String TASK_NOT_FOUND_MESSAGE = "Задание с идентификатором %s не найдено";
    @Value("${kafka.clientTopic}")
    private String topic;

    @LogExecutionTime
    @LogException
    @LogReturning
    @Override
    public Task create(Task task) {
        task.setState("CREATED");
        return repository.save(task);
    }

    @LogExecutionTime
    @LogException
    @LogReturning
    @Override
    public Task getTask(Long id) {
        Optional<Task> byId = repository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            String message = String.format(TASK_NOT_FOUND_MESSAGE, id);
            throw new TaskNotFoundException(message);
        }
    }

    @LogExecutionTime
    @LogException
    @Override
    public void update(Long id, Task inputTask) {
        Task taskFromDB = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND_MESSAGE, id)));

        taskFromDB.setState("UPDATED");
        taskFromDB.setDescription(inputTask.getDescription());
        taskFromDB.setTitle(inputTask.getTitle());
        taskFromDB.setUserId(inputTask.getUserId());

        Task saved = repository.save(taskFromDB);
        if (saved.getState().equals("UPDATED")) {
            producer.sendTo(topic, new MessageDto(id, saved.getState()));
        }
    }

    @LogExecutionTime
    @LogException
    @Override
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND_MESSAGE, id)));
        repository.deleteById(id);
    }

    @LogExecutionTime
    @LogException
    @LogReturning
    @Override
    public List<Task> list() {
        return repository.findAll();
    }
}
