package com.buapp.abonnement_service.controller;

import com.buapp.abonnement_service.dto.AbonnementRequest;
import com.buapp.abonnement_service.dto.AbonnementResponse;
import com.buapp.abonnement_service.service.AbonnementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abonnements")
@RequiredArgsConstructor
public class AbonnementController {

    private final AbonnementService abonnementService;
    @PostMapping
    public ResponseEntity<AbonnementResponse> createAbonnement(@RequestBody AbonnementRequest request) {
        AbonnementResponse response = abonnementService.createAbonnement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAbonnement(@PathVariable Long id) {
        abonnementService.cancelAbonnement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<AbonnementResponse>> getUserAbonnements(@PathVariable String userEmail) {
        List<AbonnementResponse> abonnements = abonnementService.getAbonnementsForUser(userEmail);
        return ResponseEntity.ok(abonnements);
    }
}
