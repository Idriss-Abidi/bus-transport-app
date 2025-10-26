package com.buapp.trajetservice.station.repository;

import com.buapp.trajetservice.station.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
//    List<Station> findByTrajetId(Long trajetId);
    @Query("SELECT s FROM Station s JOIN s.trajetStations ts WHERE ts.trajet.id = :trajetId ORDER BY ts.ordreDansTrajet ASC")
    List<Station> findByTrajetId(@Param("trajetId") Long trajetId);
}
