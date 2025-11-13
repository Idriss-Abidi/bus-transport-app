package com.buapp.trajetservice.trajet.repository;

import com.buapp.trajetservice.trajet.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Long> {
	List<Trajet> findByCity_Id(Long cityId);

}
