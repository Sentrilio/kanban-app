package com.domko.kanbanbackendapp.service;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Board;
import com.domko.kanbanbackendapp.model.SeriesSet;
import com.domko.kanbanbackendapp.model.Trend;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrendService {

    void updateBoardTrends(Board board);

    void updateTrendForColumn(BColumn column);

    ResponseEntity<SeriesSet> getTrendsFromLastDays(Long boardId);

    void prepareColumnTrends(Board board, SeriesSet seriesSet);

    void prepareTrendLines(Board board, SeriesSet seriesSet);

    double calulateAverage(double sum, int size);

    List<Double> getBestFitLine(List<Double> list);

    int calulatePreviousValue(int colIndex, int valueIndex, SeriesSet seriesSet);

    void updateNumberOfTasks(long boardId);

    void incrementArrivalOfTasks(long boardId);
}
