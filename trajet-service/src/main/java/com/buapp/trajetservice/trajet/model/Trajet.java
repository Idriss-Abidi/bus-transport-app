package com.buapp.trajetservice.trajet.model;

import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.util.List;

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

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(name = "duree_estimee")
    private Duration dureeEstimee;

    @OneToMany(mappedBy = "trajet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordreDansTrajet ASC")
    private List<TrajetStation> trajetStations;
}
