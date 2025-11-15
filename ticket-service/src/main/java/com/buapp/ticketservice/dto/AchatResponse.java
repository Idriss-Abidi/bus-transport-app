package com.buapp.ticketservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class AchatResponse {
    @Schema(example = "10")
    Long id;
    @Schema(example = "1")
    Long ticketId;
    @Schema(example = "42")
    Long userId;
    String userName;
    @Schema(example = "true")
    Boolean valid;
    @Schema(example = "2025-11-13T14:00:00")
    LocalDateTime createdAt;
    @Schema(example = "2025-11-13T15:30:00")
    LocalDateTime validatedAt;
}
