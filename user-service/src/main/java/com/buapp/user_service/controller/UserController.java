package com.buapp.user_service.controller;

import com.buapp.user_service.dto.UserResponse;
import com.buapp.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

/**
 * @author DELL
 **/
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User read APIs")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List users", description = "Get all users")
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user", description = "Get a user by id")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
