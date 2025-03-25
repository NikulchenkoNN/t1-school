package ru.home_work.t1_school.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.service.TaskService;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class TaskControllerImpl {

    private final TaskService service;

    @PostMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task Task = service.create(task);
        return ResponseEntity.ok(task);
    }

    @GetMapping(value = "/tasks/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = service.getTask(id);
        return ResponseEntity.ok(task);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody Task task) {
        Task updated = service.update(id, task);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> deleteTask(@RequestParam Long id) {
        Task deleted = service.delete(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Task>> listTasks() {
        Collection<Task> list = service.list();
        return ResponseEntity.ok(list);
    }
}
