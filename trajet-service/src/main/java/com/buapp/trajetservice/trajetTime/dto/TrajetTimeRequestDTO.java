package com.buapp.trajetservice.trajetTime.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetTimeRequestDTO {
    @NotNull
    private Long trajetId;

    @NotNull
    private LocalTime startTime;
}
