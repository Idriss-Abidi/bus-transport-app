package com.buapp.notificationservice.notification.abonnementnotification;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "abonnement_notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AbonnementNotification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long abonnementId;
    private Long userId;
    private String type; // subscription type, e.g., MONTHLY/YEARLY
    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime receivedAt;

    @PrePersist
    void prePersist() {
        if (receivedAt == null) receivedAt = LocalDateTime.now();
    }
}
