package com.buapp.trajetservice.trajetstation.mapper;

import com.buapp.trajetservice.station.model.Station;
import com.buapp.trajetservice.trajet.model.Trajet;
import com.buapp.trajetservice.trajetstation.dto.TrajetStationRequestDTO;
import com.buapp.trajetservice.trajetstation.dto.TrajetStationResponseDTO;
import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import org.springframework.stereotype.Component;

@Component
public class TrajetStationMapper {

    public TrajetStation toEntity(TrajetStationRequestDTO dto, Trajet trajet, Station station) {
        return TrajetStation.builder()
                .trajet(trajet)
                .station(station)
                .ordreDansTrajet(dto.getOrdreDansTrajet())
                .estimatedMinutes(dto.getEstimatedMinutes())
                .build();
    }

    public TrajetStationResponseDTO toDTO(TrajetStation entity) {
        return TrajetStationResponseDTO.builder()
                .id(entity.getId())
                .trajetId(entity.getTrajet().getId())
                .stationId(entity.getStation().getId())
                .stationName(entity.getStation().getNom())
                .ordreDansTrajet(entity.getOrdreDansTrajet())
                .estimatedMinutes(entity.getEstimatedMinutes())
                .build();
    }
}
