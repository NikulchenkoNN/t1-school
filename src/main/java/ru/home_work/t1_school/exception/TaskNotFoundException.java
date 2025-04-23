package ru.home_work.t1_school.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String s) {
        super(s);
    }

    public TaskNotFoundException() {
    }
}
