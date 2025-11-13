package com.buapp.trajetservice.trajetTime.repository;

import com.buapp.trajetservice.trajetTime.model.TrajetTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrajetTimeRepository extends JpaRepository<TrajetTime, Long> {
	List<TrajetTime> findByTrajet_IdOrderByStartTimeAsc(Long trajetId);
}
