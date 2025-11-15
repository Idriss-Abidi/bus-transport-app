package com.buapp.user_service.service;

import com.buapp.user_service.dto.AuthResponse;
import com.buapp.user_service.dto.LoginRequest;
import com.buapp.user_service.dto.RegisterRequest;
import com.buapp.user_service.dto.UserResponse;
import com.buapp.user_service.enums.UserRole;
import com.buapp.user_service.model.User;
import com.buapp.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public AuthResponse register(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if(request.getRole()== null || request.getRole().describeConstable().isEmpty()){
            request.setRole(UserRole.PASSAGER);
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setNom(request.getName());
        user.setEmail(request.getEmail());
        user.setMotDePasse(request.getPassword());
        user.setUserRole(request.getRole());

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        return new AuthResponse(token, toUserResponse(user));
    }

    public AuthResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (user.getMotDePasse() == null || !user.getMotDePasse().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = UUID.randomUUID().toString();
        return new AuthResponse(token, toUserResponse(user));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNom(user.getNom());
        response.setEmail(user.getEmail());
        response.setRole(user.getUserRole() != null ? user.getUserRole().name() : "USER");
        return response;
    }
}
