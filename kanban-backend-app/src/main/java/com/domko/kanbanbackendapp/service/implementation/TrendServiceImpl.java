package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.TrendRepository;
import com.domko.kanbanbackendapp.service.TrendService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@org.springframework.transaction.annotation.Transactional
public class TrendServiceImpl implements TrendService {

    @Autowired
    private TrendRepository trendRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Override
    public Trend save(Trend trend) {
        return trendRepository.save(trend);
    }

    public Optional<Trend> findById(Long id) {
        return trendRepository.findById(id);
    }

    public Optional<Trend> findByColumnIdAndDate(Long columnId, Date date) {
        return trendRepository.findByColumnIdAndDate(columnId, date);
    }


    public void addTrend(Task task) {
        Optional<Trend> trend = findByColumnIdAndDate(task.getColumn().getId(), new Date());
        if (trend.isPresent()) {
            System.out.println("column trend found");
            trend.get().setElements(trend.get().getElements() + 1);
            save(trend.get());
        } else {
            System.out.println("column trend not found");
            Trend trendToSave = new Trend();
            trendToSave.setColumn(task.getColumn());
            trendToSave.setDate(new Date());
            trendToSave.setElements(1);
            save(trendToSave);
        }
    }

    public ResponseEntity<SeriesSet> getTrendsFromLastDays(Long boardId, int days) {
        SeriesSet seriesSet = new SeriesSet();
        List<String> dates = new ArrayList<>();
        Date date = new Date();
        LocalDateTime dateTime = new LocalDateTime(date);
        dateTime = dateTime.minusDays(days);
        for (int i = 1; i <= days; i++) {
            dates.add(dateTime.plusDays(i).toString());
        }
        seriesSet.setDates(dates);
        Optional<Board> board = boardRepository.findById(boardId);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        board.ifPresent(value -> value.getColumns().forEach(column -> {
            List<Trend> trends = trendRepository.findAllByColumnIdOrderByDate(column.getId());
            int j = 0;
            Series series = new Series(days);
            series.setName(column.getName());
            System.out.println("for column: " + column.getName());
            for (int i = 0; i < days; i++) {
                if (j == trends.size()) {
                    break;
                }
                System.out.println("comparison: " + dates.get(i).substring(0,10) + " vs " +
                        trends.get(j).getDate());
                if (dates.get(i).substring(0,10).equals(simpleDateFormat.format(trends.get(j).getDate()))) {
                    series.add(i, trends.get(j).getElements());
                    j++;
                    System.out.println("the same!");
                }
            }
            seriesSet.add(series);
        }));
        System.out.println(seriesSet);
        return new ResponseEntity<>(seriesSet, HttpStatus.OK);
    }

}
