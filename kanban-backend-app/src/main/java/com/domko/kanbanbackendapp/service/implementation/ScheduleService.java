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

import java.util.List;
import java.util.Optional;
import java.util.Random;

//@Component
@Service
@Transactional
public class ScheduleService {
    private final BoardStatisticsRepository boardStatisticsRepository;
    private final BoardRepository boardRepository;
    private final BoardServiceImpl boardService;

    @Autowired
    public ScheduleService(BoardStatisticsRepository boardStatisticsRepository, BoardRepository boardRepository,
                           BoardServiceImpl boardService) {
        this.boardStatisticsRepository = boardStatisticsRepository;
        this.boardRepository = boardRepository;
        this.boardService = boardService;
    }

    //    @Scheduled(cron = "0 0 23 * * ?")
//    @Scheduled(cron = "0 35 14 * * ?")//it worked at 14:35:00
    private void createAndFillStatisticsForAllBoardsInThePast() {
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
                boardStatisticsRepository.save(boardStatistics);
                startDate = startDate.plusDays(1);
            }
        });
        System.out.println("filled statistics");
    }

    @Scheduled(cron = "0 0 16 * * ?")
    private void createStatisticsEntityForTomorrow() {
//        List<Long> allIds = boardRepository.findAllIds();
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeTomorrow = new DateTime().plusDays(1);
        allBoards.forEach(board -> {
            System.out.println("liczba zadań: " + board.getNumberOfTasks());
            BoardStatistics boardStatistics = new BoardStatistics();
            boardStatistics.setBoard(board);
            boardStatistics.setNumberOfTasks(boardService.getNumberOfTasks(board.getId()));
            boardStatistics.setArrivalOfTasks(0);
            boardStatistics.setDate(dateTimeTomorrow.toDate());
//            boardStatisticsRepository.save(boardStatistics);
        });
        System.out.println("Date tomorrow: " + dateTimeTomorrow.toDate());
        System.out.println("created statistics field for tomorrow");

//        allIds.forEach(id -> {
//            System.out.println(id);
//            Optional<Board> board = boardRepository.findById(id);
//            if (board.isPresent()) {

//                System.out.println("id: "+board.get().getId());
//                System.out.println("name: "+board.get().getName());
//                System.out.println("create date: "+board.get().getCreateDate());
//                System.out.println("teams: "+board.get().getTeam());
//                System.out.println("stats: "+board.get().getStatistics());
//                System.out.println("number of columns: "+board.get().getColumns().size());
//                System.out.println("number of columns: "+board.get().getNumberOfTasks());
//            }
//            BoardStatistics boardStatistics = new BoardStatistics();
//            boardStatistics.setBoard(board);
//            boardStatistics.setNumberOfTasks(boardService.getNumberOfTasks(board.getId()));
//            boardStatistics.setArrivalOfTasks(0);
//            boardStatistics.setDate(dateTimeTomorrow.toDate());
//            boardStatisticsRepository.save(boardStatistics);
//        });

    }


//    private double getAverageOfPreviousTasks(Board board) {
//        List<BoardStatistics> statistics = boardStatisticsRepository.findAllByBoardIdAndDateBeforeOrderByDate(board.getId(), new Date());
//        return statistics
//                .stream()
//                .mapToDouble(BoardStatistics::getNumberOfTasks)
//                .sum() / statistics.size();
//    }

}