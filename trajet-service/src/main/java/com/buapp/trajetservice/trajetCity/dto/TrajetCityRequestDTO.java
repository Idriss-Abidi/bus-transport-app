package com.buapp.trajetservice.trajetCity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetCityRequestDTO {
    @NotBlank(message = "cityName is required")
    private String cityName;

    @NotNull(message = "priceInDhs is required")
    @DecimalMin(value = "0.00", message = "priceInDhs must be >= 0")
    private BigDecimal priceInDhs;
}
