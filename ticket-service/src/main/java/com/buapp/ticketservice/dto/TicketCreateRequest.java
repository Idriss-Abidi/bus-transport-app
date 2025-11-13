package com.buapp.ticketservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketCreateRequest {
    @NotNull
    @Positive
    private Long trajetId;

    @Size(max = 255)
    private String description;
}
