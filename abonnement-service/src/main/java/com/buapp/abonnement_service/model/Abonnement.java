package com.buapp.abonnement_service.model;

import com.buapp.abonnement_service.enums.AbonnementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author DELL
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

@Entity
@Table(name = "abonnements")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private AbonnementType type; // MONTHLY or YEARLY

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder.Default
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}
