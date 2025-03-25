package ru.home_work.t1_school.exception.hadler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {
    private int errorCode;
    private String errorMessage;
}
