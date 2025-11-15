package com.buapp.userservice.controller;

import com.buapp.userservice.dto.UserCreateRequest;
import com.buapp.userservice.dto.UserResponse;
import com.buapp.userservice.dto.UserUpdateRequest;
import com.buapp.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Users", description = "Simple CRUD for users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest req) {
        return userService.create(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public UserResponse update(@PathVariable @Positive Long id, @Valid @RequestBody UserUpdateRequest req) {
        return userService.update(id, req);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserResponse get(@PathVariable @Positive Long id) {
        return userService.get(id);
    }

    @GetMapping
    @Operation(summary = "List users")
    public List<UserResponse> list() {
        return userService.list();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        userService.delete(id);
    }
}
