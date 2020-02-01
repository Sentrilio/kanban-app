package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BoardStatistics;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.BoardStatisticsRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

//@Component
@Service
@Transactional
public class ScheduleService {
    private final BoardStatisticsRepository boardStatisticsRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public ScheduleService(BoardStatisticsRepository boardStatisticsRepository, BoardRepository boardRepository) {
        this.boardStatisticsRepository = boardStatisticsRepository;
        this.boardRepository = boardRepository;
    }

    //    @Scheduled(cron = "0 0 23 * * ?")
    @Scheduled(cron = "0 30 12 * * ?")//it worked at 14:35:00
    private void createAndFillStatisticsForAllBoardsInThePastTillToday() {
        Random random = new Random();
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeTomorrow = new DateTime();
        allBoards.forEach(board -> {
            DateTime startDate = new DateTime(board.getCreateDate());
            while (startDate.isBefore(dateTimeTomorrow)) {
                BoardStatistics boardStatistics = new BoardStatistics();
                boardStatistics.setBoard(board);
//                boardStatistics.setNumberOfTasks(board.getNumberOfTasks());
                boardStatistics.setNumberOfTasks(random.nextInt(20));
                boardStatistics.setArrivalOfTasks(random.nextInt(5));
                boardStatistics.setDate(startDate.toDate());
                System.out.println(startDate.toDate());
                boardStatisticsRepository.save(boardStatistics);
                startDate = startDate.plusDays(1);
            }
        });
        System.out.println("filled statistics");
    }

    @Scheduled(cron = "0 57 12 * * ?")
    private void createStatisticsEntityForTomorrow() {
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeTomorrow = new DateTime().plusDays(1);
        allBoards.forEach(board -> {
            BoardStatistics boardStatistics = new BoardStatistics();
            boardStatistics.setBoard(board);
            boardStatistics.setNumberOfTasks(board.getNumberOfTasks());
            boardStatistics.setArrivalOfTasks(0);
            boardStatistics.setDate(dateTimeTomorrow.toDate());
            boardStatisticsRepository.save(boardStatistics);
        });
        System.out.println("Date tomorrow: " + dateTimeTomorrow.toDate());
        System.out.println("created statistics field for tomorrow");

    }


//    private double getAverageOfPreviousTasks(Board board) {
//        List<BoardStatistics> statistics = boardStatisticsRepository.findAllByBoardIdAndDateBeforeOrderByDate(board.getId(), new Date());
//        return statistics
//                .stream()
//                .mapToDouble(BoardStatistics::getNumberOfTasks)
//                .sum() / statistics.size();
//    }

}