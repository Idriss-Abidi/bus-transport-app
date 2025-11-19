package com.buapp.abonnement_service.controller;

import com.buapp.abonnement_service.dto.AbonnementRequest;
import com.buapp.abonnement_service.dto.AbonnementResponse;
import com.buapp.abonnement_service.service.AbonnementService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abonnements")
@RequiredArgsConstructor
@Tag(name = "Abonnements", description = "Manage user abonnements/subscriptions")
public class AbonnementController {

    private final AbonnementService abonnementService;
    @PostMapping
    @Operation(summary = "Create abonnement", description = "Create a new abonnement for a user")
    public ResponseEntity<AbonnementResponse> createAbonnement(@RequestBody AbonnementRequest request) {
        AbonnementResponse response = abonnementService.createAbonnement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel abonnement", description = "Cancel an existing abonnement by its id")
    public ResponseEntity<Void> cancelAbonnement(@PathVariable Long id) {
        abonnementService.cancelAbonnement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List user abonnements", description = "Get all abonnements for a given user ID")
    public ResponseEntity<List<AbonnementResponse>> getUserAbonnements(@PathVariable Long userId) {
        List<AbonnementResponse> abonnements = abonnementService.getAbonnementsForUser(userId);
        return ResponseEntity.ok(abonnements);
    }
}
