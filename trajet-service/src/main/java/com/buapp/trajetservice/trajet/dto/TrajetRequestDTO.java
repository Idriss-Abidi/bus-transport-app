package com.buapp.trajetservice.trajet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Duration;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetRequestDTO {
    @NotBlank(message = "Le nom du trajet ne peut pas être nul")
    private String nomTrajet;
    @NotBlank(message = "La source du trajet ne peut pas être nulle")
    private String source;
    @NotBlank(message = "La destination du trajet ne peut pas être nulle")
    private String destination;
    @NotNull(message = "La ville du trajet ne peut pas être nulle")
    private Long cityId;
    @NotNull(message = "La durée estimée du trajet ne peut pas être nulle")
    private Duration dureeEstimee;
    @NotNull(message = "La liste des stations ne peut pas être nulle")
    private List<Long> stationIds; // ordered list of station IDs
    // For each station in stationIds (same order) the estimated minutes from the previous stop
    // For the first station this is minutes from trajet start to first station
    private List<Integer> stationDurations;

    // Deprecated: start times now managed via TrajetTime endpoints. Field ignored if provided.
    @Deprecated
    private List<String> startTimes;
}
