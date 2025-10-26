package com.buapp.trajetservice.trajet.service;

import com.buapp.trajetservice.trajet.dto.*;
import com.buapp.trajetservice.trajet.mapper.TrajetMapper;
import com.buapp.trajetservice.trajet.model.Trajet;
import com.buapp.trajetservice.trajet.repository.TrajetRepository;
import com.buapp.trajetservice.station.model.Station;
import com.buapp.trajetservice.station.repository.StationRepository;
import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrajetService {
    @Autowired
    private TrajetRepository trajetRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private TrajetMapper trajetMapper;

    public TrajetResponseDTO createTrajet(TrajetRequestDTO dto) {
        List<Station> stations = stationRepository.findAllById(dto.getStationIds());
        Trajet trajet = trajetMapper.toEntity(dto, stations);
        trajetRepository.save(trajet);
        return trajetMapper.toDTO(trajet);
    }

    public List<TrajetResponseDTO> getAllTrajets() {
        return trajetRepository.findAll().stream()
                .map(trajetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addStationToTrajet(Long trajetId, Long stationId, int position) {
        Trajet trajet = trajetRepository.findById(trajetId)
                .orElseThrow(() -> new RuntimeException("Trajet not found"));

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station not found"));

        List<TrajetStation> trajetStations = trajet.getTrajetStations();

        // 1️⃣ Shift ordre of stations that come after the new position
        trajetStations.stream()
                .filter(ts -> ts.getOrdreDansTrajet() >= position)
                .forEach(ts -> ts.setOrdreDansTrajet(ts.getOrdreDansTrajet() + 1));

        // 2️⃣ Create the new TrajetStation link
        TrajetStation newLink = TrajetStation.builder()
                .trajet(trajet)
                .station(station)
                .ordreDansTrajet(position)
                .build();

        // 3️⃣ Add and re-sort
        trajetStations.add(newLink);
        trajetStations.sort(Comparator.comparing(TrajetStation::getOrdreDansTrajet));

        // 4️⃣ Save (cascade = ALL will persist child links)
        trajetRepository.save(trajet);
    }

}
