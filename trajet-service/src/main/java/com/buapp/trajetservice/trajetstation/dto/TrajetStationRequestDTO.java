package com.buapp.trajetservice.trajetstation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetStationRequestDTO {
    @NotNull
    private Long trajetId;
    @NotNull
    private Long stationId;
    @NotNull
    private Integer ordreDansTrajet;

    private Integer estimatedMinutes;
}
