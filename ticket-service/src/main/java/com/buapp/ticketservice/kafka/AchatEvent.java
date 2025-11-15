package com.buapp.ticketservice.kafka;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class AchatEvent {
	Long achatId;
	Long userId;
	String userName;
	Long ticketId;

	String ticketDescription;

	Long trajetId;
	String nomTrajet;
	String cityName;
	BigDecimal priceInDhs;

	LocalDateTime createdAt; // achat creation time
}
