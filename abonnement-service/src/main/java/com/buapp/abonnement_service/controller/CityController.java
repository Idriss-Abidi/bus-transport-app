package com.buapp.abonnement_service.controller;

import com.buapp.abonnement_service.model.City;
import com.buapp.abonnement_service.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abonnements/cities")
@RequiredArgsConstructor
@Tag(name = "Cities", description = "Manage cities for abonnements")
public class CityController {

    private final CityRepository cityRepository;

    @GetMapping
    @Operation(summary = "List all cities", description = "Get all available cities")
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get city", description = "Get a city by ID")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        return cityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create city", description = "Create a new city")
    public ResponseEntity<City> createCity(@RequestBody City city) {
        city.calculateYearlyPrice();
        City savedCity = cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update city", description = "Update an existing city")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City city) {
        if (!cityRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        city.setId(id);
        city.calculateYearlyPrice();
        City updatedCity = cityRepository.save(city);
        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete city", description = "Delete a city by ID")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        if (cityRepository.existsById(id)) {
            cityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
