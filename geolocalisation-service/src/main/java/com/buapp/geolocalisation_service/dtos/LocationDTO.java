package com.buapp.geolocalisation_service.dtos;

import lombok.Data;

@Data
public class LocationDTO {
    private Long busId;
    private double latitude;
    private double longitude;
    private long timestamp;
}
