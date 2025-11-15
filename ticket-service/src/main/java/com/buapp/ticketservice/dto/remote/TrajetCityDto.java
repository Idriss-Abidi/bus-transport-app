package com.buapp.ticketservice.dto.remote;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrajetCityDto {
    private Long id;
    // Bind remote JSON field "cityName" to this client field "name"
    @JsonProperty("cityName")
    private String name;
    private BigDecimal priceInDhs;
}
