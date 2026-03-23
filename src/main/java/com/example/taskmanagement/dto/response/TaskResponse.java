package com.example.taskmanagement.dto.response;

import com.example.taskmanagement.model.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Long assigneeId;
    private String assigneeName;
    private LocalDate dueDate;
    private TaskStatus status;
}