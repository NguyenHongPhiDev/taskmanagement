package com.example.taskmanagement.common;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
// Tạo 1 dạng response chung cho cả hệ thống dùng chung
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
