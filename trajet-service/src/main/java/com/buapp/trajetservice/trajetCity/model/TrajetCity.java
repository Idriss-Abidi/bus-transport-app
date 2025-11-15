package com.buapp.trajetservice.trajetCity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import com.buapp.trajetservice.trajet.model.Trajet;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trajet_cities")
public class TrajetCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city_name", nullable = false, unique = true)
    private String cityName;

    // Fixed price in DHS (decimal). Use precision/scale to avoid floating point issues.
    @Column(name = "price_in_dhs", nullable = false, precision = 19, scale = 2)
    private BigDecimal priceInDhs;

    // Proper relation to Trajet; Trajet has a ManyToOne back reference 'city'
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Trajet> trajets; // list of all trajets for this city
}
