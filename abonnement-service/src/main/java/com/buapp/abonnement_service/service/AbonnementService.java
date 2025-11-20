package com.buapp.abonnement_service.service;

import com.buapp.abonnement_service.dto.AbonnementRequest;
import com.buapp.abonnement_service.dto.AbonnementResponse;
import com.buapp.abonnement_service.model.Abonnement;
import com.buapp.abonnement_service.model.City;
import com.buapp.abonnement_service.enums.AbonnementType;
import com.buapp.abonnement_service.repository.AbonnementRepository;
import com.buapp.abonnement_service.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import com.buapp.abonnement_service.kafka.AbonnementEvent;
import com.buapp.abonnement_service.kafka.KafkaProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AbonnementService {

    private final AbonnementRepository abonnementRepository;
    private final CityRepository cityRepository;
        private final KafkaProducer kafkaProducer;

    @Transactional
    public AbonnementResponse createAbonnement(AbonnementRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        // Deactivate all previous active abonnements for this user
        List<Abonnement> activeAbonnements = abonnementRepository.findByUserId(request.getUserId());
        activeAbonnements.forEach(ab -> {
            if (ab.getActive() != null && ab.getActive()) {
                ab.setActive(false);
                abonnementRepository.save(ab);
            }
        });

        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate endDate = request.getType() == AbonnementType.MONTHLY ? startDate.plusMonths(1)
                : startDate.plusYears(1);

        Abonnement abonnement = Abonnement.builder()
                .userId(request.getUserId())
                .city(city)
                .type(request.getType())
                .startDate(startDate)
                .endDate(endDate)
                .active(true)
                .build();

        abonnement = abonnementRepository.save(abonnement);

        // Publish Kafka event for notification-service
        AbonnementEvent event = AbonnementEvent.builder()
                .abonnementId(abonnement.getId())
                .userId(abonnement.getUserId())
                .userName(null) // Can be enriched by notification service if needed
                .type(abonnement.getType())
                .startDate(abonnement.getStartDate())
                .endDate(abonnement.getEndDate())
                .build();
        kafkaProducer.sendEvent(event);

        return AbonnementResponse.builder()
                .id(abonnement.getId())
                .userId(abonnement.getUserId())
                .type(abonnement.getType())
                .startDate(abonnement.getStartDate())
                .endDate(abonnement.getEndDate())
                .active(abonnement.getActive())
                .city(abonnement.getCity())
                .build();
    }

    @Transactional
    public void cancelAbonnement(Long abonnementId) {
        Abonnement abonnement = abonnementRepository.findById(abonnementId)
                .orElseThrow(() -> new RuntimeException("Abonnement not found"));
        abonnement.setActive(false);
        abonnementRepository.save(abonnement);
    }

    public List<AbonnementResponse> getAbonnementsForUser(Long userId) {
        return abonnementRepository.findByUserId(userId)
                .stream()
                .map(ab -> AbonnementResponse.builder()
                        .id(ab.getId())
                        .userId(ab.getUserId())
                        .type(ab.getType())
                        .startDate(ab.getStartDate())
                        .endDate(ab.getEndDate())
                        .active(ab.getActive())
                        .city(ab.getCity())
                        .build())
                .collect(Collectors.toList());
    }

    public AbonnementResponse getCurrentAbonnement(Long userId) {
        return abonnementRepository.findByUserId(userId)
                .stream()
                .filter(ab -> ab.getActive() != null && ab.getActive())
                .findFirst()
                .map(ab -> AbonnementResponse.builder()
                        .id(ab.getId())
                        .userId(ab.getUserId())
                        .type(ab.getType())
                        .startDate(ab.getStartDate())
                        .endDate(ab.getEndDate())
                        .active(ab.getActive())
                        .city(ab.getCity())
                        .build())
                .orElse(null);
    }
}
