package com.buapp.trajetservice.trajetstation.service;

import com.buapp.trajetservice.station.model.Station;
import com.buapp.trajetservice.station.repository.StationRepository;
import com.buapp.trajetservice.trajet.model.Trajet;
import com.buapp.trajetservice.trajet.repository.TrajetRepository;
import com.buapp.trajetservice.trajetstation.dto.TrajetStationRequestDTO;
import com.buapp.trajetservice.trajetstation.dto.TrajetStationResponseDTO;
import com.buapp.trajetservice.trajetstation.mapper.TrajetStationMapper;
import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import com.buapp.trajetservice.trajetstation.repository.TrajetStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrajetStationService {
    private final TrajetStationRepository trajetStationRepository;
    private final TrajetRepository trajetRepository;
    private final StationRepository stationRepository;
    private final TrajetStationMapper trajetStationMapper;

    /**
     * Insert a station into a trajet sequence with the following rules:
     * - A station can appear only once per trajet (enforced also by DB unique constraint).
     * - If the station already exists at the requested position -> do nothing and return existing.
     * - If the station exists at a different position -> move it to the new position and shift stations at or after the new position by +1.
     * - If the station is new and position < end -> insert and shift stations at or after position by +1.
     * - If the requested position is > current size + 1 -> append at the end.
     */
    @Transactional
    public TrajetStationResponseDTO create(TrajetStationRequestDTO dto) {
        Trajet trajet = trajetRepository.findById(dto.getTrajetId())
                .orElseThrow(() -> new RuntimeException("Trajet not found"));
        Station station = stationRepository.findById(dto.getStationId())
                .orElseThrow(() -> new RuntimeException("Station not found"));

        int requestedPos = dto.getOrdreDansTrajet();
        if (requestedPos <= 0) {
            throw new RuntimeException("ordreDansTrajet must be >= 1");
        }

        List<TrajetStation> existing = trajetStationRepository.findByTrajet_IdOrderByOrdreDansTrajetAsc(trajet.getId());
        Optional<TrajetStation> existingForStation = existing.stream()
                .filter(ts -> ts.getStation().getId().equals(station.getId()))
                .findFirst();

        int size = existing.size();
        // Normalize requested position if beyond end (append)
        if (requestedPos > size + 1) {
            requestedPos = size + 1;
        }

        if (existingForStation.isPresent()) {
            TrajetStation current = existingForStation.get();
            int currentPos = current.getOrdreDansTrajet();
            if (currentPos == requestedPos) {
                // No change needed
                return trajetStationMapper.toDTO(current);
            }
            // Move station to new position: treat as insertion at requestedPos
            for (TrajetStation other : existing) {
                if (other.getId().equals(current.getId())) continue;
                if (other.getOrdreDansTrajet() >= requestedPos) {
                    other.setOrdreDansTrajet(other.getOrdreDansTrajet() + 1);
                }
            }
            current.setOrdreDansTrajet(requestedPos);
            trajetStationRepository.saveAll(existing);
            return trajetStationMapper.toDTO(current);
        } else {
            // New station insertion
            for (TrajetStation other : existing) {
                if (other.getOrdreDansTrajet() >= requestedPos) {
                    other.setOrdreDansTrajet(other.getOrdreDansTrajet() + 1);
                }
            }
            TrajetStation newEntity = TrajetStation.builder()
                    .trajet(trajet)
                    .station(station)
                    .ordreDansTrajet(requestedPos)
                    .estimatedMinutes(dto.getEstimatedMinutes())
                    .build();
            trajetStationRepository.saveAll(existing); // shift updates
            newEntity = trajetStationRepository.save(newEntity);
            return trajetStationMapper.toDTO(newEntity);
        }
    }

    public List<TrajetStationResponseDTO> listByTrajet(Long trajetId) {
        return trajetStationRepository.findByTrajet_IdOrderByOrdreDansTrajetAsc(trajetId)
                .stream().map(trajetStationMapper::toDTO).collect(Collectors.toList());
    }

    public TrajetStationResponseDTO get(Long id) {
        Optional<TrajetStation> opt = trajetStationRepository.findById(id);
        return opt.map(trajetStationMapper::toDTO).orElse(null);
    }

    @Transactional
    public TrajetStationResponseDTO update(Long id, TrajetStationRequestDTO dto) {
        // Delegate to create logic for repositioning semantics when changing order or station.
        TrajetStation existing = trajetStationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrajetStation not found"));

        if (!existing.getStation().getId().equals(dto.getStationId()) ||
                !existing.getTrajet().getId().equals(dto.getTrajetId())) {
            throw new RuntimeException("Changing trajet or station via update is not supported; delete and recreate instead");
        }

        int requestedPos = dto.getOrdreDansTrajet();
        if (requestedPos <= 0) {
            throw new RuntimeException("ordreDansTrajet must be >= 1");
        }

        List<TrajetStation> sequence = trajetStationRepository.findByTrajet_IdOrderByOrdreDansTrajetAsc(existing.getTrajet().getId());
        if (existing.getOrdreDansTrajet() == requestedPos) {
            existing.setEstimatedMinutes(dto.getEstimatedMinutes());
            return trajetStationMapper.toDTO(trajetStationRepository.save(existing));
        }
        // Shift stations at or after requested position
        for (TrajetStation other : sequence) {
            if (other.getId().equals(existing.getId())) continue;
            if (other.getOrdreDansTrajet() >= requestedPos) {
                other.setOrdreDansTrajet(other.getOrdreDansTrajet() + 1);
            }
        }
        existing.setOrdreDansTrajet(requestedPos);
        existing.setEstimatedMinutes(dto.getEstimatedMinutes());
        trajetStationRepository.saveAll(sequence);
        return trajetStationMapper.toDTO(trajetStationRepository.save(existing));
    }

    public void delete(Long id) {
        trajetStationRepository.deleteById(id);
    }
}
