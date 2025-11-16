package com.buapp.user_service.controller;

import com.buapp.user_service.dto.AuthResponse;
import com.buapp.user_service.dto.LoginRequest;
import com.buapp.user_service.dto.RegisterRequest;
import com.buapp.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user", description = "Register a new user and return tokens/profile")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login", description = "Authenticate with credentials and return tokens/profile")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
