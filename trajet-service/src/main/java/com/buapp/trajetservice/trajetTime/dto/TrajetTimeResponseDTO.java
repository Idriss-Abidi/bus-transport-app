package com.buapp.trajetservice.trajetTime.dto;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetTimeResponseDTO {
    private Long id;
    private Long trajetId;
    private LocalTime startTime;
}
