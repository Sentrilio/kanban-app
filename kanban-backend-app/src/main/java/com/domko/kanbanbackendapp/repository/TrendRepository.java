package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.Trend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrendRepository extends JpaRepository<Trend, Long> {

    Optional<Trend> findByColumnIdAndDate(Long columnId, Date date);

    List<Trend> findAllByColumnIdOrderByDate(Long columnId);

    @Query(value = "SELECT sum(elements) FROM trend t full outer join public.b_column c on (c.id = t.column_id)" +
            " where c.board_id=:boardId and date=:date", nativeQuery = true)
    Integer getSumOfElementsByBoardIdAndDate(long boardId, Date date);
}
