package com.buapp.abonnement_service.dto;

/**
 * @author DELL
 **/
import com.buapp.abonnement_service.enums.AbonnementType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbonnementRequest {
    private String userEmail;
    private Long cityId;
    private AbonnementType type; // MONTHLY ou YEARLY
    private LocalDate startDate; // facultatif
}
