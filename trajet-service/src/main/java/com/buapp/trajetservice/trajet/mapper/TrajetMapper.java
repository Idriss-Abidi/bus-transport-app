package com.buapp.trajetservice.trajet.mapper;

import com.buapp.trajetservice.trajet.dto.*;
import com.buapp.trajetservice.trajet.model.*;
import com.buapp.trajetservice.station.model.Station;
import com.buapp.trajetservice.trajetCity.model.TrajetCity;
import com.buapp.trajetservice.trajetTime.model.TrajetTime;
import com.buapp.trajetservice.trajetTime.repository.TrajetTimeRepository;
import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrajetMapper {

        private final TrajetTimeRepository trajetTimeRepository;

        public TrajetMapper(TrajetTimeRepository trajetTimeRepository) {
                this.trajetTimeRepository = trajetTimeRepository;
        }

        public Trajet toEntity(TrajetRequestDTO dto, List<Station> stations, TrajetCity city) {
        Trajet trajet = Trajet.builder()
                                        .nomTrajet(dto.getNomTrajet())
                .sourceStationId(dto.getSourceStationId())
                .destinationStationId(dto.getDestinationStationId())
                .dureeEstimee(dto.getDureeEstimee())
                .build();
        trajet.setCity(city);
        List<TrajetStation> trajetStations = new java.util.ArrayList<>();
        List<Integer> durations = dto.getStationDurations();

        for (int i = 0; i < dto.getStationIds().size(); i++) {
            Long id = dto.getStationIds().get(i);
            Station station = stations.stream()
                    .filter(s -> s.getId().equals(id))
                    .findFirst()
                    .orElseThrow();

            Integer estimated = null;
            if (durations != null && i < durations.size()) {
                estimated = durations.get(i);
            }

            TrajetStation ts = TrajetStation.builder()
                    .trajet(trajet)
                    .station(station)
                    .ordreDansTrajet(i + 1)
                    .estimatedMinutes(estimated)
                    .build();
            trajetStations.add(ts);
        }

        trajet.setTrajetStations(trajetStations);
        return trajet;
    }

    public TrajetResponseDTO toDTO(Trajet trajet) {
        List<String> stationNames = trajet.getTrajetStations().stream()
                .sorted((a, b) -> a.getOrdreDansTrajet() - b.getOrdreDansTrajet())
                .map(ts -> ts.getStation().getNom())
                .collect(Collectors.toList());

        // Find source and destination station names
        String sourceStationName = trajet.getTrajetStations().stream()
                .filter(ts -> ts.getStation().getId().equals(trajet.getSourceStationId()))
                .findFirst()
                .map(ts -> ts.getStation().getNom())
                .orElse(null);
                
        String destinationStationName = trajet.getTrajetStations().stream()
                .filter(ts -> ts.getStation().getId().equals(trajet.getDestinationStationId()))
                .findFirst()
                .map(ts -> ts.getStation().getNom())
                .orElse(null);

        TrajetResponseDTO.TrajetResponseDTOBuilder builder = TrajetResponseDTO.builder()
                .id(trajet.getId())
                .nomTrajet(trajet.getNomTrajet())
                .sourceStationId(trajet.getSourceStationId())
                .source(sourceStationName)
                .destinationStationId(trajet.getDestinationStationId())
                .destination(destinationStationName)
        .cityId(trajet.getCity() != null ? trajet.getCity().getId() : null)
                .dureeEstimee(trajet.getDureeEstimee())
                .stationNames(stationNames);

        // derive startTimes from TrajetTime if repository is available and trajet has id
        if (trajet.getId() != null) {
            java.util.List<String> times = trajetTimeRepository.findByTrajet_IdOrderByStartTimeAsc(trajet.getId())
                    .stream().map(tt -> tt.getStartTime().toString()).collect(java.util.stream.Collectors.toList());
            builder.startTimes(times);
        }
        return builder.build();
    }
}
