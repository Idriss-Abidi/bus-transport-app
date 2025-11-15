package com.buapp.ticketservice.service;

import com.buapp.ticketservice.dto.*;
import com.buapp.ticketservice.exception.DuplicateTicketException;
import com.buapp.ticketservice.exception.NotFoundException;
import com.buapp.ticketservice.kafka.KafkaProducer;
import com.buapp.ticketservice.kafka.AchatEvent;
import com.buapp.ticketservice.dto.remote.TrajetDto;
import com.buapp.ticketservice.dto.remote.TrajetCityDto;
import com.buapp.ticketservice.model.Achat;
import com.buapp.ticketservice.model.Ticket;
import com.buapp.ticketservice.repository.AchatRepository;
import com.buapp.ticketservice.mapper.TicketMapper;
import com.buapp.ticketservice.mapper.AchatMapper;
import com.buapp.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AchatRepository achatRepository;
    private final RestTemplate restTemplate;
    private final TicketMapper ticketMapper;
    private final AchatMapper achatMapper;
    private final KafkaProducer kafkaProducer;

    @Value("${trajet.service.base-url:http://trajet-service:4000}")
    private String trajetServiceBaseUrl;

    @Value("${user.service.base-url:http://api-gateway:4004/api}")
    private String userServiceBaseUrl;

    public TicketResponse create(TicketCreateRequest req) {
        // Enforce: only one ticket per trajet
            if (ticketRepository.existsByTrajetId(req.getTrajetId())) {
                throw new DuplicateTicketException("Ticket already exists for this trajet");
        }
    BigDecimal price = fetchPriceForTrajet(req.getTrajetId());
    // Fetch trajet and city to denormalize names
    TrajetDto trajet = fetchTrajet(req.getTrajetId());
    TrajetCityDto city = fetchCity(trajet.getCityId());

    Ticket ticket = Ticket.builder()
        .trajetId(req.getTrajetId())
        .nomTrajet(trajet.getNomTrajet())
        .cityName(city.getName())
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

    public List<TicketResponse> listTicketsByUser(Long userId) {
        var achats = achatRepository.findByUserId(userId);
        var ticketIds = achats.stream().map(Achat::getTicketId).distinct().toList();
        var tickets = ticketRepository.findAllById(ticketIds);
        return tickets.stream().map(ticketMapper::toResponse).collect(Collectors.toList());
    }

    public List<TicketResponse> listActiveTicketsByUser(Long userId) {
        var achats = achatRepository.findByUserIdAndValidTrue(userId);
        var ticketIds = achats.stream().map(Achat::getTicketId).distinct().toList();
        var tickets = ticketRepository.findAllById(ticketIds);
        return tickets.stream().map(ticketMapper::toResponse).collect(Collectors.toList());
    }

    // Achat operations (purchase)
    public AchatResponse createAchat(AchatCreateRequest req) {
        // Ensure ticket exists
        if (!ticketRepository.existsById(req.getTicketId())) {
            throw new NotFoundException("Ticket not found: " + req.getTicketId());
        }
        // Ensure user exists via API Gateway -> user-service
        // Get user name through gateway and validate existence
        String userName = fetchUserName(req.getUserId());
        Achat achat = Achat.builder()
                .ticketId(req.getTicketId())
                .userId(req.getUserId())
                .userName(userName)
                .valid(true)
                .createdAt(LocalDateTime.now())
                .build();
    Achat saved = achatRepository.save(achat);

    // Build rich event for notification
    Ticket ticket = ticketRepository.findById(req.getTicketId())
        .orElseThrow(() -> new NotFoundException("Ticket not found: " + req.getTicketId()));

    AchatEvent event = AchatEvent.builder()
        .achatId(saved.getId())
        .userId(saved.getUserId())
        .userName(userName)
        .ticketId(saved.getTicketId())
        .ticketDescription(ticket.getDescription())
        .trajetId(ticket.getTrajetId())
        .nomTrajet(ticket.getNomTrajet())
        .cityName(ticket.getCityName())
        .priceInDhs(ticket.getPrice())
        .createdAt(saved.getCreatedAt())
        .build();
    kafkaProducer.sendEvent(event);
    return toResponse(saved);
    }

    public AchatResponse validateAchat(Long id) {
        Achat achat = achatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Achat not found: " + id));
        achat.setValid(false);
        achat.setValidatedAt(LocalDateTime.now());
        Achat saved = achatRepository.save(achat);
        return toResponse(saved);
    }

    public AchatResponse getAchat(Long id) {
        return achatRepository.findById(id)
                .map(achatMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Achat not found: " + id));
    }

    public java.util.List<AchatResponse> listAchats() {
        return achatMapper.toResponses(achatRepository.findAll());
    }

    public java.util.List<AchatResponse> listAchatsByUser(Long userId) {
        return achatMapper.toResponses(achatRepository.findByUserId(userId));
    }

    public java.util.List<AchatResponse> listActiveAchatsByUser(Long userId) {
        return achatMapper.toResponses(achatRepository.findByUserIdAndValidTrue(userId));
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

    private TrajetDto fetchTrajet(Long trajetId) {
        String url = trajetServiceBaseUrl + "/trajets/" + trajetId;
        ResponseEntity<TrajetDto> resp = restTemplate.getForEntity(url, TrajetDto.class);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new NotFoundException("Trajet not found: " + trajetId);
        }
        return resp.getBody();
    }

    private TrajetCityDto fetchCity(Long cityId) {
        String url = trajetServiceBaseUrl + "/trajet-cities/" + cityId;
        ResponseEntity<TrajetCityDto> resp = restTemplate.getForEntity(url, TrajetCityDto.class);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new NotFoundException("City not found: " + cityId);
        }
        return resp.getBody();
    }

    private TicketResponse toResponse(Ticket t) {
        return TicketResponse.builder()
                .id(t.getId())
                .trajetId(t.getTrajetId())
                .nomTrajet(t.getNomTrajet())
                .cityName(t.getCityName())
                .price(t.getPrice())
                .description(t.getDescription())
                .createdAt(t.getCreatedAt())
                .build();
    }

    private static AchatResponse toResponse(Achat a) {
        return AchatResponse.builder()
                .id(a.getId())
                .ticketId(a.getTicketId())
                .userId(a.getUserId())
                .userName(a.getUserName())
                .valid(a.getValid())
                .createdAt(a.getCreatedAt())
                .validatedAt(a.getValidatedAt())
                .build();
    }

    private String fetchUserName(Long userId) {
        try {
            String url = userServiceBaseUrl + "/users/" + userId;
            // Use a flexible DTO to capture various possible name fields
            ResponseEntity<com.fasterxml.jackson.databind.JsonNode> resp = restTemplate.getForEntity(url, com.fasterxml.jackson.databind.JsonNode.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new NotFoundException("User not found: " + userId);
            }
            var node = resp.getBody();
            String name = null;
            if (node.hasNonNull("name")) name = node.get("name").asText();
            else if (node.hasNonNull("fullName")) name = node.get("fullName").asText();
            else if (node.hasNonNull("username")) name = node.get("username").asText();
            if (name == null || name.isBlank()) name = "user-" + userId;
            return name;
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            throw new NotFoundException("User not found: " + userId);
        } catch (org.springframework.web.client.RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to validate user at user-service");
        }
    }

}