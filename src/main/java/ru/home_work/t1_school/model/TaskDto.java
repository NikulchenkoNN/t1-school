package ru.home_work.t1_school.model;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Long userId;
}
