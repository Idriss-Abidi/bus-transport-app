package com.buapp.trajetservice.station.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationRequestDTO {
    @NotBlank(message = "Le nom de la station ne peut pas être nul")
    private String nom;
}
