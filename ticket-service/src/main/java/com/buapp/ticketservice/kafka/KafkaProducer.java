package com.buapp.ticketservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, AchatEvent> kafkaTemplate;

    @Value("ticket")
    private String topic;

    public void sendEvent(AchatEvent event) {
        try {
            kafkaTemplate.send(topic, event);
            log.info("Sent achat event to topic {}: achatId={} userId={} ticketId={}", topic, event.getAchatId(), event.getUserId(), event.getTicketId());
        } catch (Exception e) {
            log.error("Failed to send Kafka message", e);
            throw new RuntimeException(e);
        }
    }
}
