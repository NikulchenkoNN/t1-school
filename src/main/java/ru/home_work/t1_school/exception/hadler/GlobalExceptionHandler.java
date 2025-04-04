package ru.home_work.t1_school.exception.hadler;

import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.home_work.t1_school.exception.TaskNotFoundException;

import java.net.HttpURLConnection;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorDetails handleTaskNotFoundException(TaskNotFoundException ex) {
        return new ErrorDetails(HttpURLConnection.HTTP_NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StaleObjectStateException.class)
    @ResponseBody
    public ErrorDetails handleTaskNotFoundException() {
        return new ErrorDetails(HttpURLConnection.HTTP_BAD_REQUEST, "При создании Задания нельзя указывать идентификатор");
    }
}
