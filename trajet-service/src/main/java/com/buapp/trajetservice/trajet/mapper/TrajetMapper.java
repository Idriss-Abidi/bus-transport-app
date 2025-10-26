package com.buapp.trajetservice.trajet.mapper;

import com.buapp.trajetservice.trajet.dto.*;
import com.buapp.trajetservice.trajet.model.*;
import com.buapp.trajetservice.station.model.Station;
import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrajetMapper {

    public Trajet toEntity(TrajetRequestDTO dto, List<Station> stations) {
        Trajet trajet = Trajet.builder()
                .source(dto.getSource())
                .destination(dto.getDestination())
                .dureeEstimee(dto.getDureeEstimee())
                .build();

        List<TrajetStation> trajetStations = dto.getStationIds().stream()
                .map(id -> {
                    Station station = stations.stream()
                            .filter(s -> s.getId().equals(id))
                            .findFirst()
                            .orElseThrow();
                    return TrajetStation.builder()
                            .trajet(trajet)
                            .station(station)
                            .ordreDansTrajet(dto.getStationIds().indexOf(id) + 1)
                            .build();
                })
                .collect(Collectors.toList());

        trajet.setTrajetStations(trajetStations);
        return trajet;
    }

    public TrajetResponseDTO toDTO(Trajet trajet) {
        List<String> stationNames = trajet.getTrajetStations().stream()
                .sorted((a, b) -> a.getOrdreDansTrajet() - b.getOrdreDansTrajet())
                .map(ts -> ts.getStation().getNom())
                .collect(Collectors.toList());

        return TrajetResponseDTO.builder()
                .id(trajet.getId())
                .source(trajet.getSource())
                .destination(trajet.getDestination())
                .dureeEstimee(trajet.getDureeEstimee())
                .stationNames(stationNames)
                .build();
    }
}
