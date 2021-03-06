package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.BoardStatisticRepository;
import com.domko.kanbanbackendapp.repository.RoleRepository;
import com.domko.kanbanbackendapp.repository.TrendRepository;
import com.domko.kanbanbackendapp.service.BoardService;
import com.domko.kanbanbackendapp.service.ScheduleService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    private final BoardStatisticRepository boardStatisticRepository;
    private final RoleRepository roleRepository;
    private final BoardRepository boardRepository;
    private final TrendRepository trendRepository;
    private final BoardService boardService;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ScheduleServiceImpl(BoardStatisticRepository boardStatisticRepository, RoleRepository roleRepository, BoardRepository boardRepository,
                               TrendRepository trendRepository, BoardService boardService) {
        this.boardStatisticRepository = boardStatisticRepository;
        this.roleRepository = roleRepository;
        this.boardRepository = boardRepository;
        this.trendRepository = trendRepository;
        this.boardService = boardService;
    }


    //today
    @Scheduled(cron = "5 0 0 * * ?")
    private void updateAllStatisticsForToday() {
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeToday = new DateTime();
        allBoards.forEach(board -> {
            Optional<BoardStatistic> statistic = boardStatisticRepository.findByBoardIdAndDate(
                    board.getId(), dateTimeToday.toDate());
            if (statistic.isPresent()) {
                statistic.get().setNumberOfTasks(boardService.getNumberOfTasks(board));
                boardStatisticRepository.save(statistic.get());
            } else {
                BoardStatistic boardStatistic = new BoardStatistic();
                boardStatistic.setBoard(board);
                boardStatistic.setNumberOfTasks(boardService.getNumberOfTasks(board));
                boardStatistic.setDate(dateTimeToday.toDate());
                boardStatisticRepository.save(boardStatistic);
            }
        });
        System.out.println("Board statistic for today updated: " + dateTimeToday);
    }

    @Scheduled(cron = "5 0 0 * * ?")
    private void updateAllColumnTrendsForToday() {
        List<Board> allBoards = boardRepository.findAll();
        DateTime dateTimeToday = new DateTime();
        allBoards.forEach(board -> {
            board.getColumns().forEach(column -> {
                Optional<Trend> trend = trendRepository.findByColumnIdAndDate(column.getId(), dateTimeToday.toDate());
                if (trend.isPresent()) {
                    trend.get().setElements(column.getTasks().size());
                    trendRepository.save(trend.get());
                } else {
                    Trend trendToSave = new Trend();
                    trendToSave.setColumn(column);
                    trendToSave.setDate(dateTimeToday.toDate());
                    trendToSave.setElements(column.getTasks().size());
                    trendRepository.save(trendToSave);
                }
            });

        });
        System.out.println("Board statistic for today updated: " + dateTimeToday);
    }

    //tomorrow
    @Scheduled(cron = "0 30 23 * * ?")
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

    @Scheduled(cron = "0 30 23 * * ?")
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

    //past till tomorrow
//    @EventListener(ApplicationReadyEvent.class)
    private void fillColumnTrendsAndBoardStatistics() {
        createAndFillTrendsForAllBoardsInThePastTillTomorrow();
        createAndFillStatisticsForAllBoardsInThePastTillToday();
    }

    @EventListener(ApplicationReadyEvent.class)
    private void fillRoles() {
        if (roleRepository.findAll().size() != 3) {
            roleRepository.deleteAll();
            Role userRole = new Role();
            userRole.setName(ERole.ROLE_USER);
            Role modRole = new Role();
            modRole.setName(ERole.ROLE_MODERATOR);
            Role adminRole = new Role();
            adminRole.setName(ERole.ROLE_ADMIN);
            roleRepository.save(userRole);
            roleRepository.save(modRole);
            roleRepository.save(adminRole);
            System.out.println("roles created at first start of the application");
        }
    }

    private void createAndFillTrendsForAllBoardsInThePastTillTomorrow() {
        trendRepository.deleteAll();
        Random random = new Random();
        List<Board> allBoards = boardRepository.findAll();
        DateTime dayAfterTomorrow = new DateTime().plusDays(1);
        String dateTimeToday = simpleDateFormat.format(new DateTime().toDate());
        allBoards.forEach(board -> {
            board.getColumns().forEach(column -> {
                DateTime startDate = new DateTime(board.getCreateDate());
                while (startDate.isBefore(dayAfterTomorrow)) {
                    if (simpleDateFormat.format(startDate.toDate()).equals(dateTimeToday)) {
                        Trend trendToSave = new Trend();
                        trendToSave.setColumn(column);
                        trendToSave.setDate(startDate.toDate());
                        trendToSave.setElements(column.getTasks().size());
                        trendRepository.save(trendToSave);
                    } else {
                        Trend trendToSave = new Trend();
                        trendToSave.setColumn(column);
                        trendToSave.setDate(startDate.toDate());
                        trendToSave.setElements(random.nextInt(5) + 1);
                        trendRepository.save(trendToSave);
                    }
                    startDate = startDate.plusDays(1);
                }
            });

        });
        System.out.println("filled previous days with random trends");
    }

    private void createAndFillStatisticsForAllBoardsInThePastTillToday() {
        boardStatisticRepository.deleteAll();
        Random random = new Random();
        List<Board> allBoards = boardRepository.findAll();
        DateTime dayAfterTomorrow = new DateTime().plusDays(1);
        String dateTimeToday = simpleDateFormat.format(new DateTime().toDate());
        allBoards.forEach(board -> {
            DateTime startDate = new DateTime(board.getCreateDate());
            while (startDate.isBefore(dayAfterTomorrow)) {
                if (simpleDateFormat.format(startDate.toDate()).equals(dateTimeToday)) {
                    BoardStatistic boardStatistic = new BoardStatistic();
                    boardStatistic.setBoard(board);
                    boardStatistic.setNumberOfTasks(boardService.getNumberOfTasks(board));
                    boardStatistic.setArrivalOfTasks(0);
                    boardStatistic.setDate(startDate.toDate());
                    boardStatisticRepository.save(boardStatistic);
                } else {
                    try {
                        BoardStatistic boardStatistic = new BoardStatistic();
                        boardStatistic.setBoard(board);
                        int sum = trendRepository.getSumOfElementsByBoardIdAndDate(board.getId(), startDate.toDate());
                        boardStatistic.setNumberOfTasks(sum);
                        boardStatistic.setArrivalOfTasks(random.nextInt(5));
                        boardStatistic.setDate(startDate.toDate());
                        boardStatisticRepository.save(boardStatistic);
                    } catch (Exception e) {
                        System.out.println("something is wrong with getSumOfElements" + board.getId() + " - " + board.getName());
                        //board has to have at least 1 column to work getSumOfElements works properly
//                        e.printStackTrace();
                    }
                }
                startDate = startDate.plusDays(1);
            }
        });
        System.out.println("filled previous days with random statistics");
    }


}