package com.buapp.ticketservice.mapper;

import com.buapp.ticketservice.dto.AchatResponse;
import com.buapp.ticketservice.model.Achat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AchatMapper {

    public AchatResponse toResponse(Achat a) {
        if (a == null) return null;
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

    public List<AchatResponse> toResponses(List<Achat> achats) {
        if (achats == null) return List.of();
        return achats.stream().filter(Objects::nonNull).map(this::toResponse).collect(Collectors.toList());
    }
}
