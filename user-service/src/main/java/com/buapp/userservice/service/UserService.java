package com.buapp.userservice.service;

import com.buapp.userservice.dto.UserCreateRequest;
import com.buapp.userservice.dto.UserResponse;
import com.buapp.userservice.dto.UserUpdateRequest;
import com.buapp.userservice.model.User;
import com.buapp.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse create(UserCreateRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .fullName(req.getFullName())
                .build();
        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UserUpdateRequest req) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // uniqueness checks if email changed
        if (!user.getEmail().equalsIgnoreCase(req.getEmail()) && userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserResponse get(Long id) {
        return userRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .createdAt(u.getCreatedAt())
                .build();
    }
}
