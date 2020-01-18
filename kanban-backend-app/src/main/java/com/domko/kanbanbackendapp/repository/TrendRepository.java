package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Trend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrendRepository extends JpaRepository<Trend, Long> {

    Optional<Trend> findByColumnIdAndDate(Long columnId, Date date);

    List<Integer> findByDate(Date date);

    List<Trend> findAllByColumnIdOrderByDate(Long columnId);
}
