package com.buapp.ticketservice.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class TicketResponse {
    Long id;
    Long trajetId;
    String nomTrajet;
    String cityName;
    BigDecimal price;
    String description;
    LocalDateTime createdAt;
}
