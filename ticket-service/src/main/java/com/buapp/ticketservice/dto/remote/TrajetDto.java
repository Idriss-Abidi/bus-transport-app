package com.buapp.ticketservice.dto.remote;

import lombok.Data;

@Data
public class TrajetDto {
    private Long id;
    private Long cityId;
    private String nomTrajet;
}
