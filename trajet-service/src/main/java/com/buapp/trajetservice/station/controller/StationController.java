package com.buapp.trajetservice.station.controller;

import com.buapp.trajetservice.station.dto.*;
import com.buapp.trajetservice.station.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stations")
@Tag(name = "Station", description = "API for managing stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @PostMapping
    @Operation(summary = "Create a new station")
    public StationResponseDTO createStation(@Valid @RequestBody StationRequestDTO dto) {
        return stationService.createStation(dto);
    }

    @GetMapping
    @Operation(summary = "Get all stations")
    public List<StationResponseDTO> getAllStations() {
        return stationService.getAllStations();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing station")
    public ResponseEntity<StationResponseDTO> updateStation(@PathVariable Long id, @Valid @RequestBody StationRequestDTO dto) {
        StationResponseDTO stationResponseDTO = stationService.updateStation(id, dto);
        return ResponseEntity.ok().body(stationResponseDTO);
        }
}
