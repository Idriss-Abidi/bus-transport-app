package com.buapp.trajetservice.trajetCity.mapper;

import com.buapp.trajetservice.trajetCity.dto.TrajetCityRequestDTO;
import com.buapp.trajetservice.trajetCity.dto.TrajetCityResponseDTO;
import com.buapp.trajetservice.trajetCity.model.TrajetCity;
import org.springframework.stereotype.Component;

@Component
public class TrajetCityMapper {

    public TrajetCity toEntity(TrajetCityRequestDTO dto) {
        if (dto == null) return null;
    return TrajetCity.builder()
        .cityName(dto.getCityName())
        .priceInDhs(dto.getPriceInDhs())
        .build();
    }

    public TrajetCityResponseDTO toDTO(TrajetCity entity) {
        if (entity == null) return null;
    return TrajetCityResponseDTO.builder()
        .id(entity.getId())
        .cityName(entity.getCityName())
        .priceInDhs(entity.getPriceInDhs())
        .build();
    }
}
