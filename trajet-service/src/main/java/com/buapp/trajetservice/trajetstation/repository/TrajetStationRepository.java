package com.buapp.trajetservice.trajetstation.repository;

import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrajetStationRepository extends JpaRepository<TrajetStation, Long> {}
