package com.buapp.abonnement_service.service;

import com.buapp.abonnement_service.dto.AbonnementRequest;
import com.buapp.abonnement_service.dto.AbonnementResponse;
import com.buapp.abonnement_service.model.Abonnement;
import com.buapp.abonnement_service.model.City;
import com.buapp.abonnement_service.enums.AbonnementType;
import com.buapp.abonnement_service.repository.AbonnementRepository;
import com.buapp.abonnement_service.repository.CityRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public AbonnementResponse createAbonnement(AbonnementRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate endDate = request.getType() == AbonnementType.MONTHLY ? startDate.plusMonths(1)
                : startDate.plusYears(1);

        Abonnement abonnement = Abonnement.builder()
                .userEmail(request.getUserEmail())
                .city(city)
                .type(request.getType())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        abonnement = abonnementRepository.save(abonnement);

        return AbonnementResponse.builder()
                .id(abonnement.getId())
                .userEmail(abonnement.getUserEmail())
                .type(abonnement.getType())
                .startDate(abonnement.getStartDate())
                .endDate(abonnement.getEndDate())
                .city(abonnement.getCity())
                .build();
    }

    @Transactional
    public void cancelAbonnement(Long abonnementId) {
        Abonnement abonnement = abonnementRepository.findById(abonnementId)
                .orElseThrow(() -> new RuntimeException("Abonnement not found"));
        abonnementRepository.delete(abonnement);
    }

    public List<AbonnementResponse> getAbonnementsForUser(String userEmail) {
        return abonnementRepository.findByUserEmail(userEmail)
                .stream()
                .map(ab -> AbonnementResponse.builder()
                        .id(ab.getId())
                        .userEmail(ab.getUserEmail())
                        .type(ab.getType())
                        .startDate(ab.getStartDate())
                        .endDate(ab.getEndDate())
                        .city(ab.getCity())
                        .build())
                .collect(Collectors.toList());
    }
}
