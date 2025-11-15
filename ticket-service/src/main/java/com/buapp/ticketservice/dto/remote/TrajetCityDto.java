package com.buapp.ticketservice.dto.remote;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TrajetCityDto {
    private Long id;
    private String name;
    private BigDecimal priceInDhs;
}
