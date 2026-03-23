package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.request.CreateTaskRequest;
import com.example.taskmanagement.dto.request.UpdateTaskStatusRequest;
import com.example.taskmanagement.dto.response.TaskResponse;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.TaskStatus;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.exception.BadRequestException;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    // tạo mới
    public TaskResponse createTask(CreateTaskRequest request) {
        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getDueDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Due date cannot be in the past");
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .assignee(assignee)
                .dueDate(request.getDueDate())
                .status(TaskStatus.TODO)
                .build();

        task = taskRepository.save(task);
        return mapToResponse(task);
    }

    // update trạng thái task
    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        validateStatusTransition(task.getStatus(), request.getStatus());

        task.setStatus(request.getStatus());
        task = taskRepository.save(task);

        return mapToResponse(task);
    }

    // lấy tất cả task
    public Page<TaskResponse> getTasks(Long assigneeId, TaskStatus status, LocalDate dueDate,
                                       int page, int size, String sortDir) {
        Sort sort = Sort.by("dueDate");
        sort = "desc".equalsIgnoreCase(sortDir) ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Task> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (assigneeId != null) {
                predicates.add(cb.equal(root.get("assignee").get("id"), assigneeId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (dueDate != null) {
                predicates.add(cb.equal(root.get("dueDate"), dueDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return taskRepository.findAll(specification, pageable).map(this::mapToResponse);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return mapToResponse(task);
    }

    //check đổi trạng thái không đúng theo quy định
    private void validateStatusTransition(TaskStatus current, TaskStatus next) {
        if (current == next) {
            return;
        }

        switch (current) {
            case TODO:
                if (next != TaskStatus.IN_PROGRESS) {
                    throw new BadRequestException("Invalid transition: TODO can only move to IN_PROGRESS");
                }
                break;
            case IN_PROGRESS:
                if (next != TaskStatus.DONE) {
                    throw new BadRequestException("Invalid transition: IN_PROGRESS can only move to DONE");
                }
                break;
            case DONE:
                throw new BadRequestException("DONE status cannot be changed");
            default:
                throw new BadRequestException("Invalid status transition");
        }
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .assigneeId(task.getAssignee().getId())
                .assigneeName(task.getAssignee().getName())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }
}