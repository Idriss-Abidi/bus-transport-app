package com.buapp.trajetservice.station.service;

import com.buapp.trajetservice.exception.StationNotFoundException;
import com.buapp.trajetservice.station.dto.*;
import com.buapp.trajetservice.station.mapper.StationMapper;
import com.buapp.trajetservice.station.model.Station;
import com.buapp.trajetservice.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private StationMapper stationMapper;

    public StationResponseDTO createStation(StationRequestDTO dto) {
        Station station = stationMapper.toEntity(dto);
        stationRepository.save(station);
        return stationMapper.toDTO(station);
    }

    public List<StationResponseDTO> getAllStations() {
        return stationRepository.findAll().stream()
                .map(stationMapper::toDTO)
                .toList();
    }

    public StationResponseDTO updateStation(Long id, StationRequestDTO dto) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException("Station not found"));
        station.setNom(dto.getNom());
        stationRepository.save(station);
        return stationMapper.toDTO(station);
    }
}
