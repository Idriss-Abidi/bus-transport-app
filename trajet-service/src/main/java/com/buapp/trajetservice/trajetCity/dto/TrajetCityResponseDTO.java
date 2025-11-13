package com.buapp.trajetservice.trajetCity.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetCityResponseDTO {
    private Long id;
    private String cityName;
    private BigDecimal priceInDhs;
}
