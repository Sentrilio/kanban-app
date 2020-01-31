package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.BoardStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardStatisticsRepository extends JpaRepository<BoardStatistics,Long> {

    List<BoardStatistics> findAllByBoardIdOrderByDate(long boardId);
    List<BoardStatistics> findAllByBoardIdAndDateBeforeOrderByDate(long boardId, Date date);
    Optional<BoardStatistics> findByBoardIdAndDate(long boardId, Date date);
}
