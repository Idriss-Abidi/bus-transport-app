package com.buapp.userservice.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class UserResponse {
    Long id;
    String username;
    String email;
    String fullName;
    LocalDateTime createdAt;
}
