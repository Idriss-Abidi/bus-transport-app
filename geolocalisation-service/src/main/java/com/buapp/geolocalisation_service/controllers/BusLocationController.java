package com.buapp.geolocalisation_service.controllers;

import com.buapp.geolocalisation_service.dtos.LocationDTO;
import com.buapp.geolocalisation_service.services.BusLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class BusLocationController {

    private final BusLocationService service;

    @PostMapping("/update")
    public ResponseEntity<String> updateLocation(@RequestBody LocationDTO dto) {
        service.saveLocation(dto);
        return ResponseEntity.ok("Location saved");
    }

    @GetMapping("/latest/{busId}")
    public ResponseEntity<LocationDTO> getLatest(@PathVariable Long busId) {
        LocationDTO dto = service.getLatestLocation(busId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}
