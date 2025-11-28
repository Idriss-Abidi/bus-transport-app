package com.buapp.trajetservice.station.mapper;

import com.buapp.trajetservice.station.dto.*;
import com.buapp.trajetservice.station.model.Station;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public Station toEntity(StationRequestDTO dto) {
        return Station.builder()
                .nom(dto.getNom())
                .cityId(dto.getCityId())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    public StationResponseDTO toDTO(Station station) {
        return StationResponseDTO.builder()
                .id(station.getId())
                .nom(station.getNom())
                .cityId(station.getCityId())
                .latitude(station.getLatitude())
                .longitude(station.getLongitude())
                .build();
    }
}
