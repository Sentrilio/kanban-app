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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            seriesSet.prepareDates(board.get());
            board.get().getColumns().forEach((column) -> {
                List<Trend> trends = trendRepository.findAllByColumnIdOrderByDate(column.getId());
                int j = 0;
                Series series = new Series(seriesSet.getDates().size());
                series.setName(column.getName());
                for (int i = 0; i < seriesSet.getDates().size(); i++) {
                    if (j == trends.size()) {
                        break;
                    }
                    if (seriesSet.getDates().get(i).substring(0, 10).equals(simpleDateFormat.format(trends.get(j).getDate()))) {
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
