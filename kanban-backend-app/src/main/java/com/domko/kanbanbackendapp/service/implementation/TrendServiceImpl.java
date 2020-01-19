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
import java.util.concurrent.atomic.AtomicReference;

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
//            System.out.println("column trend found");
            trend.get().setElements(trend.get().getElements() + 1);
            save(trend.get());
        } else {
//            System.out.println("column trend not found");
            Trend trendToSave = new Trend();
            trendToSave.setColumn(task.getColumn());
            trendToSave.setDate(new Date());
            trendToSave.setElements(1);
            save(trendToSave);
        }
    }

    public ResponseEntity<SeriesSet> getTrendsFromLastDays(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        SeriesSet seriesSet = new SeriesSet();
        if (board.isPresent()) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date startDate = board.get().getCreateDate();
            List<String> dates = new ArrayList<>();
            Date today = new Date();
            LocalDateTime dateTimeStart = new LocalDateTime(startDate);
            LocalDateTime dateTimeToday = new LocalDateTime(today);
//            dateTimeStart = dateTimeStart.minusDays(days);
            dateTimeStart = dateTimeStart.withTime(0, 0, 0, 0);
            dateTimeToday = dateTimeToday.withTime(0, 0, 0, 0);

            int counter = 0;
            while (!dateTimeStart.plusDays(counter).equals(dateTimeToday.plusDays(1))) {
                System.out.println(dateTimeStart.plusDays(counter).toString());
                dates.add(dateTimeStart.plusDays(counter).toString() + "Z");
                counter++;
            }
//            days=dates.size();
            seriesSet.setDates(dates);
            AtomicReference<Integer> days = new AtomicReference<>();
            days.set(dates.size());
            board.get().getColumns().forEach((column) -> {
                List<Trend> trends = trendRepository.findAllByColumnIdOrderByDate(column.getId());
                int j = 0;
                Series series = new Series(days.get());
                series.setName(column.getName());
                for (int i = 0; i < days.get(); i++) {
                    if (j == trends.size()) {
                        break;
                    }
                    if (dates.get(i).substring(0, 10).equals(simpleDateFormat.format(trends.get(j).getDate()))) {
                        series.add(i, trends.get(j).getElements());
                        j++;
                    }
                }
                seriesSet.add(series);
            });
        }
        return new ResponseEntity<>(seriesSet, HttpStatus.OK);
    }

}
