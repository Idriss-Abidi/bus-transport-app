package com.buapp.trajetservice.trajetstation.controller;

import com.buapp.trajetservice.trajetstation.dto.TrajetStationRequestDTO;
import com.buapp.trajetservice.trajetstation.dto.TrajetStationResponseDTO;
import com.buapp.trajetservice.trajetstation.service.TrajetStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trajet-stations")
@Tag(name = "TrajetStation", description = "CRUD for trajet-station relations and estimated durations")
@RequiredArgsConstructor
public class TrajetStationController {

    private final TrajetStationService trajetStationService;

    @PostMapping
    @Operation(summary = "Create a trajet-station link")
    public ResponseEntity<TrajetStationResponseDTO> create(@Valid @RequestBody TrajetStationRequestDTO dto) {
        return ResponseEntity.ok(trajetStationService.create(dto));
    }

    @GetMapping("/by-trajet/{trajetId}")
    @Operation(summary = "List all stations (ordered) for a trajet")
    public ResponseEntity<List<TrajetStationResponseDTO>> byTrajet(@PathVariable Long trajetId) {
        return ResponseEntity.ok(trajetStationService.listByTrajet(trajetId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a trajet-station by id")
    public ResponseEntity<TrajetStationResponseDTO> get(@PathVariable Long id) {
        TrajetStationResponseDTO dto = trajetStationService.get(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order/estimated minutes of a trajet-station")
    public ResponseEntity<TrajetStationResponseDTO> update(@PathVariable Long id, @Valid @RequestBody TrajetStationRequestDTO dto) {
        return ResponseEntity.ok(trajetStationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a trajet-station link")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trajetStationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
