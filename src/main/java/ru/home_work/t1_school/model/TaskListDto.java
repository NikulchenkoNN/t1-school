package ru.home_work.t1_school.model;

import lombok.Data;

import java.util.List;

@Data
public class TaskListDto {
    private List<TaskDto> tasks;
}
