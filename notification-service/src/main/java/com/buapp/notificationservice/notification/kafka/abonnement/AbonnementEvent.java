package com.buapp.notificationservice.notification.kafka.abonnement;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AbonnementEvent {
    private Long abonnementId;
    private Long userId;
    private String type;      // store enum name as string
    private LocalDate startDate;
    private LocalDate endDate;
}
