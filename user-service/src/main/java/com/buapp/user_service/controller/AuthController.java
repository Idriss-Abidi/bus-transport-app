package com.buapp.user_service.controller;

import com.buapp.user_service.dto.AuthResponse;
import com.buapp.user_service.dto.LoginRequest;
import com.buapp.user_service.dto.RegisterRequest;
import com.buapp.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
