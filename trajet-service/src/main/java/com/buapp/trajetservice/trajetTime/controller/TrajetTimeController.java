package com.buapp.trajetservice.trajetTime.controller;

import com.buapp.trajetservice.trajet.dto.StationArrivalDTO;
import com.buapp.trajetservice.trajetTime.dto.TrajetTimeRequestDTO;
import com.buapp.trajetservice.trajetTime.dto.TrajetTimeResponseDTO;
import com.buapp.trajetservice.trajetTime.service.TrajetTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/trajet-times")
@Tag(name = "TrajetTime", description = "Manage start times for trajets and compute arrivals per station")
@RequiredArgsConstructor
public class TrajetTimeController {

    private final TrajetTimeService trajetTimeService;

    @PostMapping
    @Operation(summary = "Create a start time for a trajet")
    public ResponseEntity<TrajetTimeResponseDTO> create(@Valid @RequestBody TrajetTimeRequestDTO dto) {
        return ResponseEntity.ok(trajetTimeService.create(dto));
    }

    @GetMapping("/by-trajet/{trajetId}")
    @Operation(summary = "List start times for a trajet")
    public ResponseEntity<List<TrajetTimeResponseDTO>> listByTrajet(@PathVariable Long trajetId) {
        return ResponseEntity.ok(trajetTimeService.listByTrajet(trajetId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a start time by id")
    public ResponseEntity<TrajetTimeResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(trajetTimeService.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a start time")
    public ResponseEntity<TrajetTimeResponseDTO> update(@PathVariable Long id, @RequestParam LocalTime startTime) {
        return ResponseEntity.ok(trajetTimeService.update(id, startTime));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a start time")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trajetTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/arrivals")
    @Operation(summary = "Compute arrivals for each station for this start time")
    public ResponseEntity<List<StationArrivalDTO>> arrivals(@PathVariable Long id) {
        return ResponseEntity.ok(trajetTimeService.computeArrivals(id));
    }
}
