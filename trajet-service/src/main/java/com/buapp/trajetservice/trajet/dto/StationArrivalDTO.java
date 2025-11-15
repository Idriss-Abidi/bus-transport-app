package com.buapp.trajetservice.trajet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationArrivalDTO {
    private String stationName;
    private LocalTime arrivalTime;
}
