package com.buapp.geolocalisation_service.controllers;

import com.buapp.geolocalisation_service.models.Bus;
import com.buapp.geolocalisation_service.services.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;
    private final com.buapp.geolocalisation_service.services.SimulationService simulationService;

    @PostMapping("/{id}/simulate")
    public ResponseEntity<Void> simulateBus(@PathVariable Long id) {
        simulationService.startSimulation(id);
        return ResponseEntity.accepted().build();
    }

    @PostMapping
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus) {
        return ResponseEntity.ok(busService.createBus(bus));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBus(@PathVariable Long id) {
        return ResponseEntity.ok(busService.getBus(id));
    }

    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody Bus bus) {
        return ResponseEntity.ok(busService.updateBus(id, bus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }
}
