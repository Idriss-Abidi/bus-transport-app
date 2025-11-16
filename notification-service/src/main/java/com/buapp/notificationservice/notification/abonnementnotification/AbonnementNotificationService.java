package com.buapp.notificationservice.notification.abonnementnotification;

import com.buapp.notificationservice.notification.kafka.abonnement.AbonnementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbonnementNotificationService {
    private final AbonnementNotificationRepository repository;

    @Transactional
    public AbonnementNotification saveFromEvent(AbonnementEvent event) {
        AbonnementNotification n = AbonnementNotification.builder()
                .abonnementId(event.getAbonnementId())
                .userId(event.getUserId())
                .type(event.getType())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
        return repository.save(n);
    }

    public List<AbonnementNotification> listAll() {
        return repository.findAll();
    }

    public AbonnementNotification get(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Abonnement notification not found"));
    }
}
