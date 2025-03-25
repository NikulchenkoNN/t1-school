package ru.home_work.t1_school.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.home_work.t1_school.exception.TaskNotFoundException;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;

    @Override
    public Task create(Task task) {
        return repository.save(task);
    }

    @Override
    public Task getTask(Long id) {
        Optional<Task> byId = repository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            String message = String.format("Задание с иденотификатором %s не найдено", id);
            throw new TaskNotFoundException(message);
        }
    }

    @Override
    public Task update(Long id, Task task) {
        return repository.update(id, task);
    }

    @Override
    public Task delete(Long id) {
        return repository.deleteByTaskId(id);
    }

    @Override
    public Collection<Task> list() {
        return null;
    }
}
