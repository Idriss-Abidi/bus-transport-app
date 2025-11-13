package com.buapp.abonnement_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private double MonthlyPriceNormal;

    private double yearlyPriceNormal;


}

