package com.buapp.user_service.service;

import com.buapp.user_service.dto.UserResponse;
import com.buapp.user_service.enums.UserRole;
import com.buapp.user_service.model.User;
import com.buapp.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserResponse promoteToAdmin(Long targetUserId, Long callerId) {
        if (callerId == null) {
            throw new IllegalArgumentException("Caller ID is required");
        }
        if (targetUserId == null) {
            throw new IllegalArgumentException("Target user ID is required");
        }

        User caller = userRepository.findById(callerId)
                .orElseThrow(() -> new IllegalArgumentException("Caller not found"));

        if (caller.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Forbidden: only admins can promote users");
        }

        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User to promote not found"));
        target.setUserRole(UserRole.ADMIN);
        userRepository.save(target);
        return userService.getUserById(targetUserId);
    }

    public boolean isAdmin(Long userId) {
        if (userId == null) return false;

        return userRepository.findById(userId)
                .map(user -> user.getUserRole() == UserRole.ADMIN)
                .orElse(false);
    }
}
