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
}

