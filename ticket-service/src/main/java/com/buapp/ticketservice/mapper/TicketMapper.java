package com.buapp.ticketservice.mapper;

import com.buapp.ticketservice.dto.TicketCreateRequest;
import com.buapp.ticketservice.dto.TicketResponse;
import com.buapp.ticketservice.model.Ticket;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for Ticket <-> DTOs used by the current CRUD.
 * Note: Price is derived from Trajet's City and should be provided by the caller.
 */
@Component
public class TicketMapper {

    /**
     * Build a Ticket entity from a create request and a resolved price.
     * The price must come from Trajet Service (city price at time of creation).
     */
    public Ticket toEntity(TicketCreateRequest req, BigDecimal price) {
        if (req == null) return null;
        return Ticket.builder()
                .trajetId(req.getTrajetId())
                .price(price)
                .description(req.getDescription())
                .build();
    }

    public TicketResponse toResponse(Ticket t) {
        if (t == null) return null;
        return TicketResponse.builder()
                .id(t.getId())
                .trajetId(t.getTrajetId())
                .price(t.getPrice())
                .description(t.getDescription())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
