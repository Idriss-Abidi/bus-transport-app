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
    @NotBlank(message = "La source du trajet ne peut pas être nulle")
    private String source;
    @NotBlank(message = "La destination du trajet ne peut pas être nulle")
    private String destination;
    @NotNull(message = "La durée estimée du trajet ne peut pas être nulle")
    private Duration dureeEstimee;
    @NotNull(message = "La liste des stations ne peut pas être nulle")
    private List<Long> stationIds; // ordered list of station IDs
}
