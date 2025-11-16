package com.buapp.notificationservice.notification.kafka.achat;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NotificationEvent {
    private Long achatId;
    private Long userId;
    private String userName;
    private Long ticketId;
    private String ticketDescription;
    private Long trajetId;
    private String nomTrajet;
    private String cityName;
    private BigDecimal priceInDhs;
    private LocalDateTime createdAt; // achat creation time
}
