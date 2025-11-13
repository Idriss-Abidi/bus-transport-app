package com.buapp.abonnement_service.repository;

import com.buapp.abonnement_service.model.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author DELL
 **/
public interface AbonnementRepository extends JpaRepository<Abonnement,Long> {
    List<Abonnement> findByUserEmail(String userEmail);
}
