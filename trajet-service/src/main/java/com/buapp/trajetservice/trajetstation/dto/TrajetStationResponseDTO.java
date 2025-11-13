package com.buapp.trajetservice.trajetstation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetStationResponseDTO {
    private Long id;
    private Long trajetId;
    private Long stationId;
    private String stationName;
    private Integer ordreDansTrajet;
    private Integer estimatedMinutes;
}
