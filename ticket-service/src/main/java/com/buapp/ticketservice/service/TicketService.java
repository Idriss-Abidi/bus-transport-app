package com.buapp.ticketservice.service;

import com.buapp.ticketservice.dto.TicketCreateRequest;
import com.buapp.ticketservice.dto.TicketResponse;
import com.buapp.ticketservice.dto.TicketUpdateRequest;
import com.buapp.ticketservice.exception.DuplicateTicketException;
import com.buapp.ticketservice.exception.NotFoundException;
import com.buapp.ticketservice.model.Ticket;
import com.buapp.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RestTemplate restTemplate;

    @Value("${trajet.service.base-url:http://trajet-service:4000}")
    private String trajetServiceBaseUrl;

    public TicketResponse create(TicketCreateRequest req) {
        // Enforce: only one ticket per trajet
            if (ticketRepository.existsByTrajetId(req.getTrajetId())) {
                throw new DuplicateTicketException("Ticket already exists for this trajet");
        }
        BigDecimal price = fetchPriceForTrajet(req.getTrajetId());
        Ticket ticket = Ticket.builder()
                .trajetId(req.getTrajetId())
                .price(price)
                .description(req.getDescription())
                .build();
        Ticket saved = ticketRepository.save(ticket);
        return toResponse(saved);
    }

    public TicketResponse update(Long id, TicketUpdateRequest req) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new NotFoundException("Ticket not found"));
        ticket.setDescription(req.getDescription());
        Ticket saved = ticketRepository.save(ticket);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new NotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }

    public TicketResponse get(Long id) {
        return ticketRepository.findById(id).map(this::toResponse)
                    .orElseThrow(() -> new NotFoundException("Ticket not found"));
    }

    public List<TicketResponse> list() {
        return ticketRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TicketResponse> listByTrajet(Long trajetId) {
        return ticketRepository.findByTrajetId(trajetId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private BigDecimal fetchPriceForTrajet(Long trajetId) {
        // Step 1: fetch trajet to get cityId
    String trajetUrl = trajetServiceBaseUrl + "/trajets/" + trajetId;
        ResponseEntity<com.buapp.ticketservice.dto.remote.TrajetDto> trajetResp = restTemplate.getForEntity(trajetUrl, com.buapp.ticketservice.dto.remote.TrajetDto.class);
            if (!trajetResp.getStatusCode().is2xxSuccessful() || trajetResp.getBody() == null) {
                throw new NotFoundException("Trajet not found: " + trajetId);
        }
        Long cityId = trajetResp.getBody().getCityId();
        if (cityId == null) {
                throw new NotFoundException("Trajet has no city assigned");
        }
        // Step 2: fetch city to get priceInDhs
    String cityUrl = trajetServiceBaseUrl + "/trajet-cities/" + cityId;
        ResponseEntity<com.buapp.ticketservice.dto.remote.TrajetCityDto> cityResp = restTemplate.getForEntity(cityUrl, com.buapp.ticketservice.dto.remote.TrajetCityDto.class);
            if (!cityResp.getStatusCode().is2xxSuccessful() || cityResp.getBody() == null) {
                throw new NotFoundException("City not found: " + cityId);
        }
        return cityResp.getBody().getPriceInDhs();
    }

    private TicketResponse toResponse(Ticket t) {
        return TicketResponse.builder()
                .id(t.getId())
                .trajetId(t.getTrajetId())
                .price(t.getPrice())
                .description(t.getDescription())
                .createdAt(t.getCreatedAt())
                .build();
    }

}