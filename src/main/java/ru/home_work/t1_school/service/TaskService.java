package ru.home_work.t1_school.service;

import ru.home_work.t1_school.model.Task;

import java.util.Collection;
import java.util.List;

public interface TaskService {
    Task create(Task task);

    Task getTask(Long id);

    void update(Long id, Task task);

    void delete(Long id);

    List<Task> list();
}
