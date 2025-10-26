package com.buapp.trajetservice.trajet.dto;

import lombok.*;
import java.time.Duration;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetResponseDTO {
    private Long id;
    private String source;
    private String destination;
    private Duration dureeEstimee;
    private List<String> stationNames;
}
