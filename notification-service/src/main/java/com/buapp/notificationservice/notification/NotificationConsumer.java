package com.buapp.notificationservice.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.buapp.ticketservice.kafka.AchatEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "ticket", groupId = "notification-service-group")
    public void consume(AchatEvent event) {
        try {
            // Map producer event to local NotificationEvent DTO for persistence service
            NotificationEvent mapped = new NotificationEvent();
            mapped.setAchatId(event.getAchatId());
            mapped.setUserId(event.getUserId());
            mapped.setUserName(event.getUserName());
            mapped.setTicketId(event.getTicketId());
            mapped.setTicketDescription(event.getTicketDescription());
            mapped.setTrajetId(event.getTrajetId());
            mapped.setNomTrajet(event.getNomTrajet());
            mapped.setCityName(event.getCityName());
            mapped.setPriceInDhs(event.getPriceInDhs());
            mapped.setCreatedAt(event.getCreatedAt());

            Notification saved = notificationService.saveFromEvent(mapped);
            log.info("Stored notification id={} achatId={} user={} nomTrajet={}", saved.getId(), saved.getAchatId(), saved.getUserName(), saved.getNomTrajet());
        } catch (Exception e) {
            log.error("Failed to process notification event", e);
        }
    }
}
