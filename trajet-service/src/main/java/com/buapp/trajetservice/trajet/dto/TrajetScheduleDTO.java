package com.buapp.trajetservice.trajet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrajetScheduleDTO {
    private LocalTime startTime;
    private List<StationArrivalDTO> arrivals;
}
