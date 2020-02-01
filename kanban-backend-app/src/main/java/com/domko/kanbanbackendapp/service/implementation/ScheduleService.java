package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BoardStatistic;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.BoardStatisticRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ScheduleService {
    private final BoardStatisticRepository boardStatisticRepository;
    private final BoardRepository boardRepository;
    private final BoardServiceImpl boardService;

    @Autowired
    public ScheduleService(BoardStatisticRepository boardStatisticRepository, BoardRepository boardRepository, BoardServiceImpl boardService) {
        this.boardStatisticRepository = boardStatisticRepository;
        this.boardRepository = boardRepository;
        this.boardService = boardService;
    }

    //    @Scheduled(cron = "0 12 20 * * ?")//it worked at 12:30:00
    private void createAndFillStatisticsForAllBoardsInThePastTillToday() {
        Random random = new Random();
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeTomorrow = new DateTime();
        allBoards.forEach(board -> {
            DateTime startDate = new DateTime(board.getCreateDate());
            while (startDate.isBefore(dateTimeTomorrow)) {
                BoardStatistic boardStatistic = new BoardStatistic();
                boardStatistic.setBoard(board);
                boardStatistic.setNumberOfTasks(random.nextInt(10)+10);
                boardStatistic.setArrivalOfTasks(random.nextInt(5));
                boardStatistic.setDate(startDate.toDate());
                System.out.println(startDate.toDate());
                boardStatisticRepository.save(boardStatistic);
                startDate = startDate.plusDays(1);
            }
        });
        System.out.println("filled previous days with random statistics");
    }

    //    @Scheduled(cron = "0 0 23 * * ?")
    private void createStatisticsEntityForTomorrow() {
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeTomorrow = new DateTime().plusDays(1);
        allBoards.forEach(board -> {
            Optional<BoardStatistic> tomorrowStatistic = boardStatisticRepository.findByBoardIdAndDate(board.getId(),
                    dateTimeTomorrow.toDate());
            if (tomorrowStatistic.isEmpty()) {
                BoardStatistic boardStatistic = new BoardStatistic();
                boardStatistic.setBoard(board);
                boardStatistic.setNumberOfTasks(boardService.getNumberOfTasks(board));
                boardStatistic.setArrivalOfTasks(0);
                boardStatistic.setDate(dateTimeTomorrow.toDate());
                boardStatisticRepository.save(boardStatistic);
                System.out.println(board.getName() + ": board statistic for tomorrow created");
            } else {
                System.out.println(board.getName() + ": board statistic for tomorrow exists");
            }
        });
        System.out.println("Date tomorrow: " + dateTimeTomorrow.toDate());
        System.out.println("created statistics field for tomorrow");

    }
}