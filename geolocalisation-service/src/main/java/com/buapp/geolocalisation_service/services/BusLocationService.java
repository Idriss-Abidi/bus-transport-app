package com.buapp.geolocalisation_service.services;

import com.buapp.geolocalisation_service.dtos.LocationDTO;
import com.buapp.geolocalisation_service.models.Bus;
import com.buapp.geolocalisation_service.models.Location;
import com.buapp.geolocalisation_service.repositories.BusRepository;
import com.buapp.geolocalisation_service.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BusLocationService {

    private final BusRepository busRepository;
    private final LocationRepository locationRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public void saveLocation(LocationDTO dto) {

        // Vérifier bus existe
        Bus bus = busRepository.findById(dto.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found: " + dto.getBusId()));

        // PostgreSQL
        Location location = new Location();
        location.setBus(bus);
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setTimestamp(Instant.ofEpochMilli(dto.getTimestamp()));
        locationRepository.save(location);

        // Redis (dernière position)
        String key = "bus:" + dto.getBusId();
        redisTemplate.opsForValue().set(key, dto);

        // WebSocket broadcast
        messagingTemplate.convertAndSend("/topic/bus-location", dto);
    }

    public LocationDTO getLatestLocation(Long busId) {
        String key = "bus:" + busId;
        return (LocationDTO) redisTemplate.opsForValue().get(key);
    }
}
