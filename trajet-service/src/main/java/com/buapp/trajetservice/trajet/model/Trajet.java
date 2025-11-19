package com.buapp.trajetservice.trajet.model;

import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.util.List;
import com.buapp.trajetservice.trajetCity.model.TrajetCity;

@Entity
@Table(name = "trajets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_station_id", nullable = false)
    private Long sourceStationId;

    @Column(name = "destination_station_id", nullable = false)
    private Long destinationStationId;

    // Nouveau attribut: nom du trajet (exposé aux autres services)
    @Column(name = "nom_trajet", nullable = false)
    private String nomTrajet;

    @Column(name = "duree_estimee")
    private Duration dureeEstimee;

    // Relation to city (fixed price context). Keep column name 'cityId' for compatibility.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityId", nullable = false)
    private TrajetCity city;

    @OneToMany(mappedBy = "trajet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordreDansTrajet ASC")
    private List<TrajetStation> trajetStations;
}
