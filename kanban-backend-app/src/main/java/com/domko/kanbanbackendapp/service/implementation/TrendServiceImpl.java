package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.BoardStatisticRepository;
import com.domko.kanbanbackendapp.repository.TrendRepository;
import com.domko.kanbanbackendapp.service.TrendService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class TrendServiceImpl implements TrendService {

    private final TrendRepository trendRepository;
    private final BoardRepository boardRepository;
    private final BoardStatisticRepository boardStatisticRepository;
    private final BoardServiceImpl boardService;

    @Autowired
    public TrendServiceImpl(TrendRepository trendRepository, BoardRepository boardRepository,
                            BoardStatisticRepository boardStatisticRepository, BoardServiceImpl boardService) {
        this.trendRepository = trendRepository;
        this.boardRepository = boardRepository;
        this.boardStatisticRepository = boardStatisticRepository;
        this.boardService = boardService;
    }

    public void updateBoardTrends(Board board) {
        board.getColumns().forEach(bColumn -> {
            updateTrendForColumn(bColumn);
        });
    }

    private void updateTrendForColumn(BColumn column) {
        Date today = new Date();
        Optional<Trend> trend = trendRepository.findByColumnIdAndDate(column.getId(), today);
        if (trend.isPresent()) {
            trend.get().setElements(column.getTasks().size());
            trendRepository.save(trend.get());
        } else {
            Trend trendToSave = new Trend();
            trendToSave.setColumn(column);
            trendToSave.setDate(today);
            trendToSave.setElements(column.getTasks().size());
            trendRepository.save(trendToSave);
        }
    }

    public ResponseEntity<SeriesSet> getTrendsFromLastDays(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isPresent()) {
            SeriesSet seriesSet = new SeriesSet();
            seriesSet.prepareDates(board.get());
            prepareColumnTrends(board.get(), seriesSet);
            prepareTrendLines(board.get(), seriesSet);
            return new ResponseEntity<>(seriesSet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void prepareColumnTrends(Board board, SeriesSet seriesSet) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<BColumn> columns = new ArrayList<>(board.getColumns());
        Collections.reverse(columns);
        for (int k = 0; k < columns.size(); k++) {
            List<Trend> trends = trendRepository.findAllByColumnIdOrderByDate(columns.get(k).getId());
            int j = 0;
            ColumnSeries series = new ColumnSeries(seriesSet.getDates().size(), "area");
            series.setName(columns.get(k).getName());
            for (int i = 0; i < seriesSet.getDates().size(); i++) {
                if (j < trends.size()) {
                    if (seriesSet.getDates().get(i).substring(0, 10).equals(simpleDateFormat.format(trends.get(j).getDate()))) {
                        int previousValue = 0;
                        if (k > 0) {
                            ColumnSeries previousSeries = (ColumnSeries) seriesSet.getSeriesList().get(k - 1);
                            previousValue = previousSeries.getData().get(i);
                        }
                        series.set(i, trends.get(j).getElements() + previousValue);
                        j++;
                    } else {
                        int previousValue = calulatePreviousValue(k, i, seriesSet);
                        series.set(i, previousValue);
                    }
                } else {
                    int previousValue = calulatePreviousValue(k, i, seriesSet);
                    series.set(i, previousValue);
                }
            }
            seriesSet.add(series);
        }
    }


    private void prepareTrendLines(Board board, SeriesSet seriesSet) {
        TrendSeries trendSeries = new TrendSeries("Linia trendu", "line");
        TrendSeries arrivalOfTasksSeries = new TrendSeries("Tempo przybywania", "line");
        DateTime dateTimeTomorrow = new DateTime().plusDays(1);
        List<BoardStatistic> statistics = boardStatisticRepository.findAllByBoardIdAndDateBeforeOrderByDate(board.getId(), dateTimeTomorrow.toDate());
        List<Double> trends = new ArrayList<>();
        List<Double> arrivals = new ArrayList<>();
        double arrivalOfTaskSum = 0;
        double numberOfTaskSum = 0;
        for (int i = 0; i < statistics.size(); i++) {
            arrivalOfTaskSum += statistics.get(i).getArrivalOfTasks();
            numberOfTaskSum += statistics.get(i).getNumberOfTasks();
            arrivals.add(calulateAverage(arrivalOfTaskSum, i + 1));
            trends.add(calulateAverage(numberOfTaskSum, i + 1));
        }
        List<Double> trendsBestFitLine = getBestFitLine(trends);
        trendSeries.addAll(trendsBestFitLine);
        seriesSet.add(trendSeries);

        List<Double> arrivalsBestFitLine = getBestFitLine(arrivals);
        arrivalOfTasksSeries.addAll(arrivalsBestFitLine);
        seriesSet.add(arrivalOfTasksSeries);
    }

    private double calulateAverage(double sum, int size) {
        if (size > 0) {
            return sum / size;
        } else {
            throw new IllegalArgumentException("dividing by 0");
        }
    }

    private List<Double> getBestFitLine(List<Double> list) {
        double xSum = 0;
        double ySum = 0;
        for (int i = 0; i < list.size(); i++) {
            xSum += (i + 1);
            ySum += list.get(i);
        }
        xSum = xSum / list.size();
        ySum = ySum / list.size();
        double sumOfSquaredDeviation = 0;
        double sumOfMultipliedDeviations = 0;
        for (int i = 0; i < list.size(); i++) {
            sumOfMultipliedDeviations += ((i + 1 - xSum) * (list.get(i) - ySum));
            sumOfSquaredDeviation += Math.pow((i + 1 - xSum), 2);
        }
        double a;
        if (sumOfSquaredDeviation != 0) {
            a = sumOfMultipliedDeviations / sumOfSquaredDeviation;
            System.out.println("a = " + a);
        } else {
            System.out.println("a = 0");
            a = 0; //it will happened only if size = 1 (statistics from 1 day)
        }
        double b = ySum - (a * xSum);
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            double y = a * (i + 1) + b;
            result.add(y);
        }
        return result;
    }

    private int calulatePreviousValue(int colIndex, int valueIndex, SeriesSet seriesSet) {
        int previousValue = 0;
        if (colIndex > 0) {
            ColumnSeries previousSeries = (ColumnSeries) seriesSet.getSeriesList().get(colIndex - 1);
            previousValue = previousSeries.getData().get(valueIndex);
        }
        return previousValue;
    }

    public void updateNumberOfTasks(long boardId) {
        Date today = new Date();
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isPresent()) {
            Optional<BoardStatistic> boardStatistics = boardStatisticRepository.findByBoardIdAndDate(board.get().getId(), today);
            if (boardStatistics.isPresent()) {
                boardStatistics.get().setNumberOfTasks(boardService.getNumberOfTasks(board.get()));
                boardStatisticRepository.save(boardStatistics.get());
            } else {
                BoardStatistic boardStatistic = new BoardStatistic();
                boardStatistic.setNumberOfTasks(boardService.getNumberOfTasks(board.get()));
                boardStatistic.setBoard(board.get());
                boardStatistic.setDate(today);
                boardStatistic.setArrivalOfTasks(0);
                boardStatisticRepository.save(boardStatistic);
                System.out.println("board statistics for date " + today + " created");
            }
        }
    }

    public void incrementArrivalOfTasks(long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isPresent()) {
            Optional<BoardStatistic> boardStatistics = boardStatisticRepository.findByBoardIdAndDate(board.get().getId(), new Date());
            if (boardStatistics.isPresent()) {
                boardStatistics.get().setArrivalOfTasks(boardStatistics.get().getArrivalOfTasks() + 1);
                boardStatisticRepository.save(boardStatistics.get());
            } else {
                System.out.println("board statistics for date " + new Date() + " not found");
            }
        }
    }

}
