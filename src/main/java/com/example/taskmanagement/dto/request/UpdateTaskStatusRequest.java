package com.example.taskmanagement.dto.request;

import com.example.taskmanagement.model.TaskStatus;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class UpdateTaskStatusRequest {

    @NotNull(message = "Status is required")
    private TaskStatus status;
}