package com.buapp.abonnement_service.dto;

import com.buapp.abonnement_service.enums.AbonnementType;
import com.buapp.abonnement_service.model.City;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbonnementResponse {
    private Long id;
    private String userEmail;
    private AbonnementType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private City city;
}
