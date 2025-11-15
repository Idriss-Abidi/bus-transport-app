package com.buapp.ticketservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "achats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private Long userId;

    // Denormalized display field for notifications
    @Column(name = "user_name")
    private String userName;

    @Column(nullable = false)
    private Boolean valid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime validatedAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (valid == null) valid = true; // default: valid on creation
    }
}
