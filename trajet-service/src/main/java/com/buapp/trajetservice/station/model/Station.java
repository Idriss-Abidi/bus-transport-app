package com.buapp.trajetservice.station.model;

import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrajetStation> trajetStations;
}
