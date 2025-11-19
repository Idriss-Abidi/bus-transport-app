package com.buapp.geolocalisation_service.repositories;

import com.buapp.geolocalisation_service.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
