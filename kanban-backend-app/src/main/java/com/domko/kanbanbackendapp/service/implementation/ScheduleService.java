package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BoardStatistics;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.BoardStatisticsRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleService {
    private final BoardStatisticsRepository boardStatisticsRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public ScheduleService(BoardStatisticsRepository boardStatisticsRepository, BoardRepository boardRepository) {
        this.boardStatisticsRepository = boardStatisticsRepository;
        this.boardRepository = boardRepository;
    }

    @Scheduled(cron = "0 0 23 * * ?")
    private void create() {
        List<Board> allBoards = boardRepository.findAll();
        allBoards.forEach(board -> {
            BoardStatistics boardStatistics = new BoardStatistics();
            boardStatistics.setBoard(board);
            boardStatistics.setAverageOfTasks(getAverageOfPreviousTasks(board));
            boardStatistics.setArrivalOfTasks(0);
            DateTime dateTime = new DateTime();
            dateTime.plusDays(1);
            boardStatistics.setDate(dateTime.toDate());
            boardStatisticsRepository.save(boardStatistics);
        });
    }

    private double getAverageOfPreviousTasks(Board board) {
        return 0;
    }

}