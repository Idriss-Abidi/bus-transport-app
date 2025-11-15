package com.buapp.ticketservice.repository;

import com.buapp.ticketservice.model.Achat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchatRepository extends JpaRepository<Achat, Long> {
	List<Achat> findByUserId(Long userId);
	List<Achat> findByUserIdAndValidTrue(Long userId);
}
