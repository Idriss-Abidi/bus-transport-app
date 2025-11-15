package com.buapp.trajetservice.trajetTime.mapper;

import com.buapp.trajetservice.trajet.model.Trajet;
import com.buapp.trajetservice.trajetTime.dto.TrajetTimeRequestDTO;
import com.buapp.trajetservice.trajetTime.dto.TrajetTimeResponseDTO;
import com.buapp.trajetservice.trajetTime.model.TrajetTime;
import org.springframework.stereotype.Component;

@Component
public class TrajetTimeMapper {
    public TrajetTime toEntity(TrajetTimeRequestDTO dto, Trajet trajet) {
        return TrajetTime.builder()
                .trajet(trajet)
                .startTime(dto.getStartTime())
                .build();
    }

    public TrajetTimeResponseDTO toDTO(TrajetTime entity) {
        return TrajetTimeResponseDTO.builder()
                .id(entity.getId())
                .trajetId(entity.getTrajet().getId())
                .startTime(entity.getStartTime())
                .build();
    }
}
