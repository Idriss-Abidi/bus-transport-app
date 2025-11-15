package com.buapp.ticketservice.repository;

import com.buapp.ticketservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByTrajetId(Long trajetId);

    boolean existsByTrajetId(Long trajetId);
}
