package ru.home_work.t1_school.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.home_work.t1_school.aspect.annotations.LogMethodCallWithParams;
import ru.home_work.t1_school.model.Task;
import ru.home_work.t1_school.service.TaskService;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class TaskControllerImpl {

    private final TaskService service;

    @LogMethodCallWithParams
    @PostMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(service.create(task));
    }

    @LogMethodCallWithParams
    @GetMapping(value = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTask(id));
    }

    @LogMethodCallWithParams
    @PutMapping(value = "/tasks/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTask(@PathVariable Long id,
                                             @RequestBody Task task) {
        service.update(id, task);
        return ResponseEntity.ok("Задание " + id + " обновленно");
    }

    @LogMethodCallWithParams
    @DeleteMapping(value = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Задание " + id + " удалено");
    }

    @LogMethodCallWithParams
    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Task>> listTasks() {
        return ResponseEntity.ok(service.list());
    }
}
