package com.buapp.trajetservice.trajetTime.service;

import com.buapp.trajetservice.trajet.model.Trajet;
import com.buapp.trajetservice.trajet.repository.TrajetRepository;
import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import com.buapp.trajetservice.trajetTime.dto.TrajetTimeRequestDTO;
import com.buapp.trajetservice.trajetTime.dto.TrajetTimeResponseDTO;
import com.buapp.trajetservice.trajetTime.mapper.TrajetTimeMapper;
import com.buapp.trajetservice.trajetTime.model.TrajetTime;
import com.buapp.trajetservice.trajetTime.repository.TrajetTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrajetTimeService {

    private final TrajetTimeRepository trajetTimeRepository;
    private final TrajetRepository trajetRepository;
    private final TrajetTimeMapper trajetTimeMapper;

    public TrajetTimeResponseDTO create(TrajetTimeRequestDTO dto) {
        Trajet trajet = trajetRepository.findById(dto.getTrajetId()).orElseThrow(() -> new RuntimeException("Trajet not found"));
        TrajetTime tt = trajetTimeMapper.toEntity(dto, trajet);
        tt = trajetTimeRepository.save(tt);
        return trajetTimeMapper.toDTO(tt);
    }

    public List<TrajetTimeResponseDTO> listByTrajet(Long trajetId) {
        return trajetTimeRepository.findByTrajet_IdOrderByStartTimeAsc(trajetId).stream()
                .map(trajetTimeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TrajetTimeResponseDTO get(Long id) {
        TrajetTime tt = trajetTimeRepository.findById(id).orElseThrow(() -> new RuntimeException("TrajetTime not found"));
        return trajetTimeMapper.toDTO(tt);
    }

    @Transactional
    public TrajetTimeResponseDTO update(Long id, LocalTime newStartTime) {
        TrajetTime tt = trajetTimeRepository.findById(id).orElseThrow(() -> new RuntimeException("TrajetTime not found"));
        tt.setStartTime(newStartTime);
        return trajetTimeMapper.toDTO(tt);
    }

    public void delete(Long id) {
        trajetTimeRepository.deleteById(id);
    }

    /**
     * Compute arrival times for all stations for a given TrajetTime id.
     */
    public List<com.buapp.trajetservice.trajet.dto.StationArrivalDTO> computeArrivals(Long trajetTimeId) {
        TrajetTime tt = trajetTimeRepository.findById(trajetTimeId).orElseThrow(() -> new RuntimeException("TrajetTime not found"));
        Trajet trajet = tt.getTrajet();
        List<TrajetStation> ordered = trajet.getTrajetStations().stream()
                .sorted(Comparator.comparing(TrajetStation::getOrdreDansTrajet))
                .collect(Collectors.toList());
        long cumulative = 0L;
        LocalTime start = tt.getStartTime();
        java.util.ArrayList<com.buapp.trajetservice.trajet.dto.StationArrivalDTO> result = new java.util.ArrayList<>();
        for (TrajetStation ts : ordered) {
            int est = ts.getEstimatedMinutes() != null ? ts.getEstimatedMinutes() : 0;
            cumulative += est;
            result.add(com.buapp.trajetservice.trajet.dto.StationArrivalDTO.builder()
                    .stationName(ts.getStation().getNom())
                    .arrivalTime(start.plusMinutes(cumulative))
                    .build());
        }
        return result;
    }
}
