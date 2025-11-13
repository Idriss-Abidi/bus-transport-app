package com.buapp.trajetservice.trajetCity.controller;

import com.buapp.trajetservice.trajetCity.dto.TrajetCityRequestDTO;
import com.buapp.trajetservice.trajetCity.dto.TrajetCityResponseDTO;
import com.buapp.trajetservice.trajetCity.service.TrajetCityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trajet-cities")
@Tag(name = "TrajetCity", description = "API for managing trajet cities and prices")
@RequiredArgsConstructor
public class TrajetCityController {

    private final TrajetCityService trajetCityService;

    @PostMapping
    @Operation(summary = "Create a new trajet city with fixed price")
    public ResponseEntity<TrajetCityResponseDTO> create(@Valid @RequestBody TrajetCityRequestDTO dto) {
        return ResponseEntity.ok(trajetCityService.create(dto));
    }

    @GetMapping
    @Operation(summary = "List all trajet cities")
    public ResponseEntity<List<TrajetCityResponseDTO>> listAll() {
        return ResponseEntity.ok(trajetCityService.listAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get city by id with price")
    public ResponseEntity<TrajetCityResponseDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(trajetCityService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a city price/name")
    public ResponseEntity<TrajetCityResponseDTO> update(@PathVariable("id") Long id, @Valid @RequestBody TrajetCityRequestDTO dto) {
        return ResponseEntity.ok(trajetCityService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a city")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        trajetCityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
