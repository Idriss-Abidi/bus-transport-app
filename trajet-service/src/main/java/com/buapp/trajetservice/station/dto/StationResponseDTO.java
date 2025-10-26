package com.buapp.trajetservice.station.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationResponseDTO {
    private Long id;
    private String nom;
}
