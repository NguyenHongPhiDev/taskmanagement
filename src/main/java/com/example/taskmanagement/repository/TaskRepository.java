package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;

public interface TaskRepository extends JpaRepository<Task, Long>,JpaSpecificationExecutor<Task> {

    Page<Task> findByAssigneeIdAndStatusAndDueDate(
            Long assigneeId, TaskStatus status, LocalDate dueDate, Pageable pageable);

    Page<Task> findByAssigneeIdAndStatus(Long assigneeId, TaskStatus status, Pageable pageable);

    Page<Task> findByAssigneeIdAndDueDate(Long assigneeId, LocalDate dueDate, Pageable pageable);

    Page<Task> findByStatusAndDueDate(TaskStatus status, LocalDate dueDate, Pageable pageable);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByDueDate(LocalDate dueDate, Pageable pageable);

}