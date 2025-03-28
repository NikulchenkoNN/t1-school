package ru.home_work.t1_school.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.home_work.t1_school.aspect.annotations.LogException;
import ru.home_work.t1_school.aspect.annotations.LogExecutionTime;
import ru.home_work.t1_school.aspect.annotations.LogReturning;
import ru.home_work.t1_school.exception.TaskNotFoundException;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final String TASK_NOT_FOUND_MESSAGE = "Задание с идентификатором %s не найдено";
    @LogExecutionTime
    @LogException
    @LogReturning
    @Override
    public Task create(Task task) {
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
    public void update(Long id, Task task) {
        Optional<Task> byId = repository.findById(id);
        if (byId.isPresent()) {
            task.setId(id);
            repository.save(task);
        } else {
            String message = String.format(TASK_NOT_FOUND_MESSAGE, id);
            throw new TaskNotFoundException(message);
        }

    }

    @LogExecutionTime
    @LogException
    @Override
    public void delete(Long id) {
        Optional<Task> byId = repository.findById(id);
        if (byId.isPresent()) {
            repository.deleteById(id);
        } else {
            String message = String.format(TASK_NOT_FOUND_MESSAGE, id);
            throw new TaskNotFoundException(message);
        }
    }

    @LogExecutionTime
    @LogException
    @LogReturning
    @Override
    public Collection<Task> list() {
        return repository.findAll();
    }
}
