package com.buapp.ticketservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketUpdateRequest {
    @Size(max = 255)
    private String description;
}
