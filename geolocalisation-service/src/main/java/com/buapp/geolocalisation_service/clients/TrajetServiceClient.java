package com.buapp.geolocalisation_service.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import lombok.Data;
import java.util.List;

@Service
public class TrajetServiceClient {

    private final RestTemplate restTemplate;
    private final String trajetServiceUrl;

    public TrajetServiceClient(RestTemplate restTemplate, 
                             @Value("${trajet-service.url:http://trajet-service:4000}") String trajetServiceUrl) {
        this.restTemplate = restTemplate;
        this.trajetServiceUrl = trajetServiceUrl;
    }

    public TrajetDTO getTrajet(Long trajetId) {
        return restTemplate.getForObject(trajetServiceUrl + "/api/trajets/" + trajetId, TrajetDTO.class);
    }

    @Data
    public static class TrajetDTO {
        private Long id;
        private List<TrajetStationDTO> trajetStations;
    }

    @Data
    public static class TrajetStationDTO {
        private StationDTO station;
        private Integer ordreDansTrajet;
        private Integer estimatedMinutes;
    }

    @Data
    public static class StationDTO {
        private Long id;
        private String nom;
        private Double latitude;
        private Double longitude;
    }
}
