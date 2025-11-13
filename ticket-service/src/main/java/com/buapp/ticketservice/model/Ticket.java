package com.buapp.ticketservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A Ticket here represents a ticket product for a city (fixed price for all trajets in the same city).
 * Actual user purchases are stored in UserTicket (association table) and hold the active flag.
 */
@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // the trajet this ticket is based on (price is derived from trajet -> city)
    // enforce one Ticket per Trajet
    @Column(nullable = false, unique = true)
    private Long trajetId;

    // fixed price for this ticket (copied from trajet's city price at creation time)
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    // optional description
    private String description;

    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}
