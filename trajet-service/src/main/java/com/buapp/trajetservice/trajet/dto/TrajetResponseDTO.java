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
        // Exposing nomTrajet for ticket-service consumption
        private String nomTrajet;
    private String source;
    private String destination;
    private Long cityId;
    private Duration dureeEstimee;
    private List<String> stationNames;
    // Derived from TrajetTime entries when needed; may be null in some responses
    private List<String> startTimes;
}
