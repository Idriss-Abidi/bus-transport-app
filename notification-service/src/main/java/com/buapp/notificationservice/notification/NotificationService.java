package com.buapp.notificationservice.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    @Transactional
    public Notification saveFromEvent(NotificationEvent event) {
        Notification n = Notification.builder()
                .achatId(event.getAchatId())
                .userId(event.getUserId())
                .userName(event.getUserName())
                .ticketId(event.getTicketId())
                .ticketDescription(event.getTicketDescription())
                .trajetId(event.getTrajetId())
                .nomTrajet(event.getNomTrajet())
                .cityName(event.getCityName())
                .priceInDhs(event.getPriceInDhs())
                .achatCreatedAt(event.getCreatedAt())
                .build();
        return repository.save(n);
    }

    public List<Notification> listAll() {
        return repository.findAll();
    }

    public Notification get(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    @Transactional
    public Notification create(Notification n) {
        // receivedAt set in @PrePersist; allow direct creation for admin/testing
        return repository.save(n);
    }

    @Transactional
    public Notification update(Long id, Notification n) {
        Notification existing = get(id);
        // Update allowed fields (null-safe)
        if (n.getUserName() != null) existing.setUserName(n.getUserName());
        if (n.getTicketDescription() != null) existing.setTicketDescription(n.getTicketDescription());
        if (n.getNomTrajet() != null) existing.setNomTrajet(n.getNomTrajet());
        if (n.getCityName() != null) existing.setCityName(n.getCityName());
        if (n.getPriceInDhs() != null) existing.setPriceInDhs(n.getPriceInDhs());
        if (n.getAchatCreatedAt() != null) existing.setAchatCreatedAt(n.getAchatCreatedAt());
        // IDs (achatId, userId, ticketId, trajetId) should rarely change; update if provided
        if (n.getAchatId() != null) existing.setAchatId(n.getAchatId());
        if (n.getUserId() != null) existing.setUserId(n.getUserId());
        if (n.getTicketId() != null) existing.setTicketId(n.getTicketId());
        if (n.getTrajetId() != null) existing.setTrajetId(n.getTrajetId());
        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Notification not found");
        repository.deleteById(id);
    }
}
