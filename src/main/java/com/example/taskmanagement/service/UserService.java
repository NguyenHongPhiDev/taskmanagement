package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.request.CreateUserRequest;
import com.example.taskmanagement.dto.response.UserResponse;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.exception.DuplicateResourceException;
import com.example.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        user = userRepository.save(user);
        return mapToResponse(user);
    }

    public Page<UserResponse> getUsers(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> users;
        if (name != null && !name.trim().isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(this::mapToResponse);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}