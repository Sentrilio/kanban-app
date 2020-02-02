package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.BoardStatistic;
import com.domko.kanbanbackendapp.model.Trend;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.BoardStatisticRepository;
import com.domko.kanbanbackendapp.repository.TrendRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ScheduleService {
    private final BoardStatisticRepository boardStatisticRepository;
    private final BoardRepository boardRepository;
    private final TrendRepository trendRepository;
    private final BoardServiceImpl boardService;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ScheduleService(BoardStatisticRepository boardStatisticRepository, BoardRepository boardRepository,
                           TrendRepository trendRepository, BoardServiceImpl boardService) {
        this.boardStatisticRepository = boardStatisticRepository;
        this.boardRepository = boardRepository;
        this.trendRepository = trendRepository;
        this.boardService = boardService;
    }


    //    @Scheduled(cron = "0 20 23 * * ?")
    private void createStatisticsEntitiesForTomorrow() {
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

    //    @Scheduled(cron = "0 30 23 * * ?")
    private void createTrendsEntityForTomorrow() {
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeTomorrow = new DateTime().plusDays(1);
        allBoards.forEach(board -> {
            board.getColumns().forEach(column -> {
                Optional<Trend> tomorrowTrend = trendRepository.findByColumnIdAndDate(column.getId(), dateTimeTomorrow.toDate());
                if (tomorrowTrend.isEmpty()) {
                    Trend trendToSave = new Trend();
                    trendToSave.setColumn(column);
                    trendToSave.setDate(dateTimeTomorrow.toDate());
                    trendToSave.setElements(column.getTasks().size());
                    trendRepository.save(trendToSave);
                } else {
                    tomorrowTrend.get().setElements(column.getTasks().size());
                    trendRepository.save(tomorrowTrend.get());
                }
            });
        });
    }

    @Scheduled(cron = "0 56 1 * * ?")//it worked at 12:30:00
    private void createAndFillStatisticsForAllBoardsInThePastTillToday() {
        Random random = new Random();
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeTomorrow = new DateTime();
        allBoards.forEach(board -> {
            DateTime startDate = new DateTime(board.getCreateDate());
            while (startDate.isBefore(dateTimeTomorrow)) {
//                System.out.println(simpleDateFormat.format( startDate.toDate()));
//                System.out.println(simpleDateFormat.format(new DateTime().toDate()));
                if (simpleDateFormat.format(startDate.toDate()).equals(simpleDateFormat.format(new DateTime().toDate()))) {
                    BoardStatistic boardStatistic = new BoardStatistic();
                    boardStatistic.setBoard(board);
                    boardStatistic.setNumberOfTasks(boardService.getNumberOfTasks(board));
                    boardStatistic.setArrivalOfTasks(0);
                    boardStatistic.setDate(startDate.toDate());
                    boardStatisticRepository.save(boardStatistic);
                }
                BoardStatistic boardStatistic = new BoardStatistic();
                boardStatistic.setBoard(board);
                boardStatistic.setNumberOfTasks(random.nextInt(10) + 5);
                boardStatistic.setArrivalOfTasks(random.nextInt(5));
                boardStatistic.setDate(startDate.toDate());
                boardStatisticRepository.save(boardStatistic);
                startDate = startDate.plusDays(1);
            }
        });
        System.out.println("filled previous days with random statistics");
    }

    @Scheduled(cron = "0 56 1 * * ?")
    private void createAndFillTrendsForAllBoardsInThePastTillToday() {
        Random random = new Random();
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeTomorrow = new DateTime();
        allBoards.forEach(board -> {
            board.getColumns().forEach(column -> {
                DateTime startDate = new DateTime(board.getCreateDate());
                while (startDate.isBefore(dateTimeTomorrow)) {
                    if (simpleDateFormat.format(startDate.toDate()).equals(simpleDateFormat.format(new DateTime().toDate()))) {
                        Trend trendToSave = new Trend();
                        trendToSave.setColumn(column);
                        trendToSave.setDate(startDate.toDate());
                        trendToSave.setElements(column.getTasks().size());
                        trendRepository.save(trendToSave);
                    }else{
                        Trend trendToSave = new Trend();
                        trendToSave.setColumn(column);
                        trendToSave.setDate(startDate.toDate());
                        trendToSave.setElements(random.nextInt(3) + 1);
                        trendRepository.save(trendToSave);
                    }
                    startDate = startDate.plusDays(1);
                }
            });

        });
        System.out.println("filled previous days with random trends");
    }


}