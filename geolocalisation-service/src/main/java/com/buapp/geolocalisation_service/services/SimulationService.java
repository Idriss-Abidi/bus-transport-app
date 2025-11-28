package com.buapp.geolocalisation_service.services;

import com.buapp.geolocalisation_service.clients.TrajetServiceClient;
import com.buapp.geolocalisation_service.models.Bus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimulationService {

    private final BusService busService;
    private final BusLocationService busLocationService;
    private final TrajetServiceClient trajetServiceClient;

    @Async
    public void startSimulation(Long busId) {
        log.info("Starting simulation for bus {}", busId);
        Bus bus = busService.getBus(busId);
        if (bus.getTrajetId() == null) {
            log.error("Bus {} has no trajet assigned", busId);
            return;
        }

        TrajetServiceClient.TrajetDTO trajet = trajetServiceClient.getTrajet(bus.getTrajetId());
        if (trajet == null || trajet.getTrajetStations() == null || trajet.getTrajetStations().isEmpty()) {
            log.error("Trajet {} not found or empty", bus.getTrajetId());
            return;
        }

        List<TrajetServiceClient.TrajetStationDTO> stations = trajet.getTrajetStations();
        stations.sort(Comparator.comparing(TrajetServiceClient.TrajetStationDTO::getOrdreDansTrajet));

        for (int i = 0; i < stations.size() - 1; i++) {
            TrajetServiceClient.StationDTO start = stations.get(i).getStation();
            TrajetServiceClient.StationDTO end = stations.get(i + 1).getStation();

            if (start.getLatitude() == null || start.getLongitude() == null ||
                end.getLatitude() == null || end.getLongitude() == null) {
                log.warn("Skipping segment due to missing coordinates: {} -> {}", start.getNom(), end.getNom());
                continue;
            }

            simulateSegment(bus, start, end);
        }
        log.info("Simulation completed for bus {}", busId);
    }

    private void simulateSegment(Bus bus, TrajetServiceClient.StationDTO start, TrajetServiceClient.StationDTO end) {
        double steps = 20.0; // Number of updates between stations
        double latStep = (end.getLatitude() - start.getLatitude()) / steps;
        double lonStep = (end.getLongitude() - start.getLongitude()) / steps;

        for (int i = 0; i <= steps; i++) {
            double currentLat = start.getLatitude() + (latStep * i);
            double currentLon = start.getLongitude() + (lonStep * i);

            com.buapp.geolocalisation_service.dtos.LocationDTO locationDTO = new com.buapp.geolocalisation_service.dtos.LocationDTO();
            locationDTO.setBusId(bus.getId());
            locationDTO.setLatitude(currentLat);
            locationDTO.setLongitude(currentLon);
            locationDTO.setTimestamp(System.currentTimeMillis());

            busLocationService.saveLocation(locationDTO);

            try {
                Thread.sleep(1000); // Wait 1 second between updates
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
