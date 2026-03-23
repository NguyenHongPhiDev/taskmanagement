package com.example.taskmanagement.controller;

import com.example.taskmanagement.common.ApiResponse;
import com.example.taskmanagement.common.ApiResponseUtil;
import com.example.taskmanagement.constant.MessageConstant;
import com.example.taskmanagement.dto.request.CreateTaskRequest;
import com.example.taskmanagement.dto.request.UpdateTaskStatusRequest;
import com.example.taskmanagement.dto.response.TaskResponse;
import com.example.taskmanagement.model.TaskStatus;
import com.example.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // create task
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody CreateTaskRequest request) {

        TaskResponse task = taskService.createTask(request);

        return ResponseEntity.ok(
                ApiResponseUtil.success(MessageConstant.TASK_CREATED, task)
        );
    }

    // update status task
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request) {

        TaskResponse task = taskService.updateTaskStatus(id, request);

        return ResponseEntity.ok(
                ApiResponseUtil.success(MessageConstant.TASK_UPDATED, task)
        );
    }

    // lấy task có phân trang
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponse>>> getTasks(
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Page<TaskResponse> tasks = taskService.getTasks(assigneeId, status, dueDate, page, size,sortDir);

        return ResponseEntity.ok(
                ApiResponseUtil.success(MessageConstant.TASK_FETCHED, tasks)
        );
    }

    // lấy task detail
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {

        TaskResponse task = taskService.getTaskById(id);

        return ResponseEntity.ok(
                ApiResponseUtil.success(MessageConstant.TASK_DETAIL, task)
        );
    }
}