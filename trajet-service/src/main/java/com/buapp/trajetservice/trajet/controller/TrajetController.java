package com.buapp.trajetservice.trajet.controller;

import com.buapp.trajetservice.trajet.dto.*;
import com.buapp.trajetservice.trajet.service.TrajetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/trajets")
@Tag(name = "Trajet", description = "API for managing trajets")
public class TrajetController {

    @Autowired
    private TrajetService trajetService;

    @PostMapping
    @Operation(summary = "Create a new trajet")
    public TrajetResponseDTO createTrajet(@Valid @RequestBody TrajetRequestDTO dto) {
        return trajetService.createTrajet(dto);
    }

    @GetMapping
    @Operation(summary = "Get all trajets")
    public List<TrajetResponseDTO> getAllTrajets() {
        return trajetService.getAllTrajets();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get trajet by id")
    public TrajetResponseDTO getTrajetById(@PathVariable("id") Long id) {
        return trajetService.getTrajetById(id);
    }

    @GetMapping("/city/{cityId}")
    @Operation(summary = "Get trajets by city id")
    public List<TrajetResponseDTO> getTrajetsByCity(@PathVariable("cityId") Long cityId) {
        return trajetService.getTrajetsByCity(cityId);
    }

    @GetMapping("/{id}/schedules")
    @Operation(summary = "Get schedules (arrival times per station) for a trajet (from TrajetTime if present)")
    public List<com.buapp.trajetservice.trajet.dto.TrajetScheduleDTO> getTrajetSchedules(@PathVariable("id") Long id) {
        return trajetService.getTrajetSchedules(id);
    }
}
