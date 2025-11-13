package com.buapp.trajetservice.trajetstation.repository;

import com.buapp.trajetservice.trajetstation.model.TrajetStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrajetStationRepository extends JpaRepository<TrajetStation, Long> {
	List<TrajetStation> findByTrajet_IdOrderByOrdreDansTrajetAsc(Long trajetId);
}
