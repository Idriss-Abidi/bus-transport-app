package com.buapp.trajetservice.trajetstation.model;

import com.buapp.trajetservice.station.model.Station;
import com.buapp.trajetservice.trajet.model.Trajet;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "trajet_stations",
    uniqueConstraints = @UniqueConstraint(name = "uk_trajet_station", columnNames = {"trajet_id", "station_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "ordre_dans_trajet")
    private Integer ordreDansTrajet;

    // Estimated minutes from previous stop to reach this station.
    // For the first station this represents minutes from the trajet start to the first station.
    @Column(name = "estimated_minutes")
    private Integer estimatedMinutes;
}
