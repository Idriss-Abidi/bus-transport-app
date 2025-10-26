package com.buapp.trajetservice.trajet.repository;

import com.buapp.trajetservice.trajet.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Long> {

}
