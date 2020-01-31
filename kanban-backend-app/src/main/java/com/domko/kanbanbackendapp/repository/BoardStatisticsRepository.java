package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.BoardStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardStatisticsRepository extends JpaRepository<BoardStatistics,Long> {

    List<BoardStatistics> findAllByBoardIdOrderByDate(long boardId);
}
