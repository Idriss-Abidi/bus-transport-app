package com.buapp.geolocalisation_service.repositories;

import com.buapp.geolocalisation_service.models.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
}
