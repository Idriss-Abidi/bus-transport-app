package com.buapp.geolocalisation_service.models;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import  com.buapp.geolocalisation_service.models.Bus;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;
    private double longitude;

    private Instant timestamp;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

}
