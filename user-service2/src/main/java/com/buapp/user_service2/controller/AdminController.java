package com.buapp.user_service2.controller;

import com.buapp.user_service2.dto.UserResponse;
import com.buapp.user_service2.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/promote/{callerId}/{targetId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse promoteToAdmin(
            @PathVariable("callerId") Long callerId,
            @PathVariable("targetId") Long targetUserId) {
        return adminService.promoteToAdmin(targetUserId, callerId);
    }

    @GetMapping("/is-admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isAdmin(@PathVariable("id") Long userId) {
        return adminService.isAdmin(userId);
    }
}
