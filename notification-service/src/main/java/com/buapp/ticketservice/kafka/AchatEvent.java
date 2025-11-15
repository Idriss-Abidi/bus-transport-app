package com.buapp.ticketservice.kafka;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Mirror of the producer's event class used by ticket-service.
 * Exists here so JsonDeserializer can resolve the __TypeId__ header
 * and deserialize directly when type headers are enabled.
 */
@Data
public class AchatEvent {
    private Long achatId;
    private Long userId;
    private String userName;
    private Long ticketId;
    private String ticketDescription;
    private Long trajetId;
    private String nomTrajet;
    private String cityName;
    private BigDecimal priceInDhs;
    private LocalDateTime createdAt;
}
