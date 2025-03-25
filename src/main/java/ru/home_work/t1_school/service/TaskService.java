package ru.home_work.t1_school.service;

import ru.home_work.t1_school.model.Task;

import java.util.Collection;

public interface TaskService {
    Task create(Task task);

    Task getTask(Long id);

    Task update(Long id, Task task);

    Task delete(Long id);

    Collection<Task> list();
}
