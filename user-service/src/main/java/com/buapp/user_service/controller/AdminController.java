package com.buapp.user_service.controller;

import com.buapp.user_service.dto.UserResponse;
import com.buapp.user_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Administrative user operations")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/promote/{callerId}/{targetId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Promote to admin", description = "Promote a user to admin role; caller must be admin")
    public UserResponse promoteToAdmin(
            @PathVariable("callerId") Long callerId,
            @PathVariable("targetId") Long targetUserId) {
        return adminService.promoteToAdmin(targetUserId, callerId);
    }

    @GetMapping("/is-admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Check admin", description = "Check whether a user has admin role")
    public boolean isAdmin(@PathVariable("id") Long userId) {
        return adminService.isAdmin(userId);
    }
}
