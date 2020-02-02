package com.domko.kanbanbackendapp.repository;

import com.domko.kanbanbackendapp.model.BoardStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardStatisticRepository extends JpaRepository<BoardStatistic,Long> {

    List<BoardStatistic> findAllByBoardIdOrderByDate(long boardId);
    List<BoardStatistic> findAllByBoardIdAndDateBeforeOrderByDate(long boardId, Date date);
    Optional<BoardStatistic> findByBoardIdAndDate(long boardId, Date date);
}
