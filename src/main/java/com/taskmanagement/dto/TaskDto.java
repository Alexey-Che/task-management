package com.taskmanagement.dto;

import com.taskmanagement.entity.enums.TaskPriority;
import com.taskmanagement.entity.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class TaskDto {

    Long id;
    @NotEmpty(message = "title must not be empty")
    String title;
    String description;
    TaskStatus status;
    TaskPriority priority;
    Long authorId;
    Long executorId;
}
