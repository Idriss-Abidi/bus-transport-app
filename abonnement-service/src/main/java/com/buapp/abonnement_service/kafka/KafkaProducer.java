package com.buapp.abonnement_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, AbonnementEvent> kafkaTemplate;

    @Value("abonnement")
    private String topic;

    public void sendEvent(AbonnementEvent event) {
        try {
            kafkaTemplate.send(topic, event);
            log.info("Sent abonnement event to topic {}: abonnementId={} userId={} type={}", topic, event.getAbonnementId(), event.getUserId(), event.getType());
        } catch (Exception e) {
            log.error("Failed to send Kafka message", e);
            throw new RuntimeException(e);
        }
    }
}
