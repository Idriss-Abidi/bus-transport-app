package com.buapp.ticketservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AchatCreateRequest {
    @NotNull
    @Positive
    @Schema(example = "1")
    private Long ticketId;

    @NotNull
    @Positive
    @Schema(example = "42")
    private Long userId;
}
