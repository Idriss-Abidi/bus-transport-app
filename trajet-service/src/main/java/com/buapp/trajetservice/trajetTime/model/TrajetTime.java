package com.buapp.trajetservice.trajetTime.model;

import com.buapp.trajetservice.trajet.model.Trajet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "trajet_times",
        uniqueConstraints = @UniqueConstraint(name = "uk_trajet_start_time", columnNames = {"trajet_id", "start_time"}))
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrajetTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each start time belongs to a specific trajet
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    // Start time of the trajet (HH:mm)
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
}
