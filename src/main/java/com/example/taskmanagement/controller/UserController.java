package com.example.taskmanagement.controller;

import com.example.taskmanagement.common.ApiResponse;
import com.example.taskmanagement.common.ApiResponseUtil;
import com.example.taskmanagement.constant.MessageConstant;
import com.example.taskmanagement.dto.request.CreateUserRequest;
import com.example.taskmanagement.dto.response.UserResponse;
import com.example.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // tạo mới user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = userService.createUser(request);

        return ResponseEntity.ok(
                ApiResponseUtil.success(MessageConstant.USER_CREATED, user)
        );
    }

    // lấy user có phân trang
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserResponse> users = userService.getUsers(name, page, size);

        return ResponseEntity.ok(
                ApiResponseUtil.success(MessageConstant.USER_FETCHED, users)
        );
    }
}