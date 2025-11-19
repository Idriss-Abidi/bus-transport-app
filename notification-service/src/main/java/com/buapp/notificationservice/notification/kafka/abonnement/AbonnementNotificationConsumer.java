package com.buapp.notificationservice.notification.kafka.abonnement;

import com.buapp.notificationservice.notification.abonnementnotification.AbonnementNotification;
import com.buapp.notificationservice.notification.abonnementnotification.AbonnementNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbonnementNotificationConsumer {
    private final AbonnementNotificationService service;

    @KafkaListener(topics = "abonnement", groupId = "notification-service-group", containerFactory = "abonnementKafkaListenerContainerFactory")
    public void consume(AbonnementEvent event) {
        try {
            AbonnementNotification saved = service.saveFromEvent(event);
            log.info("Stored abonnement notification id={} abonnementId={} userId={}", saved.getId(), saved.getAbonnementId(), saved.getUserId());
        } catch (Exception e) {
            log.error("Failed to process abonnement event", e);
        }
    }
}
