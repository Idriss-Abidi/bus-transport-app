package com.buapp.notificationservice.notification;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long achatId;
    private Long userId;
    private String userName;
    private Long ticketId;
    private String ticketDescription;
    private Long trajetId;
    private String nomTrajet;
    private String cityName;

    @Column(precision = 12, scale = 2)
    private BigDecimal priceInDhs;

    private LocalDateTime achatCreatedAt; // original event timestamp
    private LocalDateTime receivedAt;     // when notification consumed

    @PrePersist
    void prePersist() {
        if (receivedAt == null) receivedAt = LocalDateTime.now();
    }
}
