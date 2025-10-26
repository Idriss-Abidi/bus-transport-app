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
@RequestMapping("/api/trajets")
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
}
