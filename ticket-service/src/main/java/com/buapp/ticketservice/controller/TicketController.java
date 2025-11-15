package com.buapp.ticketservice.controller;

import com.buapp.ticketservice.dto.*;
import com.buapp.ticketservice.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Validated
@Tag(name = "Tickets", description = "CRUD operations for ticket products (one per trajet)")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @Operation(summary = "Create a ticket for a trajet", description = "Creates one ticket for the given trajet. Fails with 409 if already exists.")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(@Valid @RequestBody TicketCreateRequest req) {
        return ticketService.create(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a ticket", description = "Updates mutable fields such as description")
    public TicketResponse update(@PathVariable @Positive Long id, @Valid @RequestBody TicketUpdateRequest req) {
        return ticketService.update(id, req);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a ticket by id")
    public TicketResponse get(@PathVariable @Positive Long id) {
        return ticketService.get(id);
    }

    @GetMapping
    @Operation(summary = "List tickets", description = "Optionally filter by trajetId")
    public List<TicketResponse> list(@RequestParam(value = "trajetId", required = false) @Positive Long trajetId) {
        if (trajetId != null) {
            return ticketService.listByTrajet(trajetId);
        }
        return ticketService.list();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        ticketService.delete(id);
    }

    // Achat endpoints under the same controller (as requested)

    @PostMapping("/achats")
    @Operation(summary = "Create achat (purchase)", description = "Creates a purchase for a ticket by a user. Initially valid=true; createdAt is set.")
    @ResponseStatus(HttpStatus.CREATED)
    public AchatResponse createAchat(@Valid @RequestBody AchatCreateRequest req) {
        return ticketService.createAchat(req);
    }

    @PatchMapping("/achats/{id}/validate")
    @Operation(summary = "Validate in bus (consume)", description = "Marks achat as not valid and sets validatedAt timestamp.")
    public AchatResponse validateAchat(@PathVariable @Positive Long id) {
        return ticketService.validateAchat(id);
    }

    @GetMapping("/achats/{id}")
    @Operation(summary = "Get achat by id")
    public AchatResponse getAchat(@PathVariable @Positive Long id) {
        return ticketService.getAchat(id);
    }

    @GetMapping("/achats")
    @Operation(summary = "List all achats")
    public java.util.List<AchatResponse> listAchats() {
        return ticketService.listAchats();
    }

    @GetMapping("/achats/users/{userId}")
    @Operation(summary = "List achats by user")
    public java.util.List<AchatResponse> listAchatsByUser(@PathVariable @Positive Long userId) {
        return ticketService.listAchatsByUser(userId);
    }

    @GetMapping("/achats/users/{userId}/active")
    @Operation(summary = "List active achats by user")
    public java.util.List<AchatResponse> listActiveAchatsByUser(@PathVariable @Positive Long userId) {
        return ticketService.listActiveAchatsByUser(userId);
    }

    // Tickets per user
    @GetMapping("/users/{userId}")
    @Operation(summary = "List all tickets bought by a user")
    public List<TicketResponse> listTicketsByUser(@PathVariable @Positive Long userId) {
        return ticketService.listTicketsByUser(userId);
    }

    @GetMapping("/users/{userId}/active")
    @Operation(summary = "List active (valid) tickets for a user")
    public List<TicketResponse> listActiveTicketsByUser(@PathVariable @Positive Long userId) {
        return ticketService.listActiveTicketsByUser(userId);
    }
}

