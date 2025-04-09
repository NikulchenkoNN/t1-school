package ru.home_work.t1_school.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String state;
    private Long userId;
}
