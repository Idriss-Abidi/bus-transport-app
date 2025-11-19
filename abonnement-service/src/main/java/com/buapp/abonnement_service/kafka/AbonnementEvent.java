package com.buapp.abonnement_service.kafka;

import lombok.Builder;
import lombok.Value;

import com.buapp.abonnement_service.enums.AbonnementType;
import java.time.LocalDate;

@Value
@Builder
public class AbonnementEvent {
	Long abonnementId;
	Long userId;
	String userName;
	AbonnementType type;
	LocalDate startDate;
	LocalDate endDate;
}
