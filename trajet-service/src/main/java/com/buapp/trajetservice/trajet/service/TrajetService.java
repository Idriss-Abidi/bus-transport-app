package com.buapp.trajetservice.trajet.service;

import com.buapp.trajetservice.trajet.dto.*;
import com.buapp.trajetservice.trajet.mapper.TrajetMapper;
import com.buapp.trajetservice.trajet.model.Trajet;
import com.buapp.trajetservice.trajet.repository.TrajetRepository;
import com.buapp.trajetservice.station.model.Station;
import com.buapp.trajetservice.station.repository.StationRepository;
import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import com.buapp.trajetservice.trajetCity.model.TrajetCity;
import com.buapp.trajetservice.trajetCity.repository.TrajetCityRepository;
import com.buapp.trajetservice.trajetTime.repository.TrajetTimeRepository;
import com.buapp.trajetservice.trajetTime.model.TrajetTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalTime;
import java.util.ArrayList;

import com.buapp.trajetservice.trajet.dto.TrajetScheduleDTO;
import com.buapp.trajetservice.trajet.dto.StationArrivalDTO;

@Service
@RequiredArgsConstructor
public class TrajetService {
    @Autowired
    private TrajetRepository trajetRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private TrajetMapper trajetMapper;
        @Autowired
        private TrajetCityRepository trajetCityRepository;
        @Autowired
        private TrajetTimeRepository trajetTimeRepository;

        public TrajetResponseDTO createTrajet(TrajetRequestDTO dto) {
                List<Station> stations = stationRepository.findAllById(dto.getStationIds());
                TrajetCity city = trajetCityRepository.findById(dto.getCityId())
                                .orElseThrow(() -> new RuntimeException("City not found"));
                        Trajet trajet = trajetMapper.toEntity(dto, stations, city);
                        trajetRepository.save(trajet);

                        // Persist provided startTimes into TrajetTime table (if any were provided)
                        if (dto.getStartTimes() != null && !dto.getStartTimes().isEmpty()) {
                                java.util.List<com.buapp.trajetservice.trajetTime.model.TrajetTime> times = new java.util.ArrayList<>();
                                for (String s : dto.getStartTimes()) {
                                        if (s == null || s.isBlank()) continue;
                                        java.time.LocalTime lt = java.time.LocalTime.parse(s);
                                        times.add(com.buapp.trajetservice.trajetTime.model.TrajetTime.builder()
                                                        .trajet(trajet)
                                                        .startTime(lt)
                                                        .build());
                                }
                                if (!times.isEmpty()) {
                                        trajetTimeRepository.saveAll(times);
                                }
                        }

                        return trajetMapper.toDTO(trajet);
    }

    public List<TrajetResponseDTO> getAllTrajets() {
        return trajetRepository.findAll().stream()
                .map(trajetMapper::toDTO)
                .collect(Collectors.toList());
    }

        public TrajetResponseDTO getTrajetById(Long id) {
                Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new RuntimeException("Trajet not found"));
                return trajetMapper.toDTO(trajet);
        }

        public List<TrajetResponseDTO> getTrajetsByCity(Long cityId) {
                return trajetRepository.findByCity_Id(cityId).stream().map(trajetMapper::toDTO).collect(Collectors.toList());
        }

    /**
     * Compute schedules for a trajet: for each start time produce arrival times for ordered stations.
     * Assumption: each TrajetStation.estimatedMinutes is minutes from previous stop to this station.
     * For the first station it is minutes from trajet start to first station.
     */
    public List<TrajetScheduleDTO> getTrajetSchedules(Long trajetId) {
        Trajet trajet = trajetRepository.findById(trajetId)
                .orElseThrow(() -> new RuntimeException("Trajet not found"));

        List<TrajetStation> ordered = trajet.getTrajetStations().stream()
                .sorted(Comparator.comparing(TrajetStation::getOrdreDansTrajet))
                .collect(Collectors.toList());

        // Use TrajetTime entries as the single source of truth
        List<LocalTime> starts = trajetTimeRepository.findByTrajet_IdOrderByStartTimeAsc(trajetId)
                .stream().map(TrajetTime::getStartTime).collect(Collectors.toList());
        if (starts == null || starts.isEmpty()) return new ArrayList<>();

        List<TrajetScheduleDTO> schedules = new ArrayList<>();

        for (LocalTime start : starts) {
            List<StationArrivalDTO> arrivals = new ArrayList<>();
            long cumulative = 0L; // minutes
            for (TrajetStation ts : ordered) {
                Integer est = ts.getEstimatedMinutes() != null ? ts.getEstimatedMinutes() : 0;
                cumulative += est;
                LocalTime arrival = start.plusMinutes(cumulative);
                arrivals.add(StationArrivalDTO.builder()
                        .stationName(ts.getStation().getNom())
                        .arrivalTime(arrival)
                        .build());
            }
            schedules.add(TrajetScheduleDTO.builder()
                    .startTime(start)
                    .arrivals(arrivals)
                    .build());
        }

        return schedules;
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
