package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.model.Trend;
import com.domko.kanbanbackendapp.repository.TrendRepository;
import com.domko.kanbanbackendapp.service.TrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class TrendServiceImpl implements TrendService {

    @Autowired
    private TrendRepository trendRepository;

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
//        Optional<Trend> trend = (new TrendId(task.getColumn().getId(), new Date(), task.getImportance()));
        if (trend.isPresent()) {
            trend.get().setElements(trend.get().getElements() + 1);
            save(trend.get());
        } else {
            Trend trendToSave = new Trend();
            trendToSave.setColumn(task.getColumn());
            trendToSave.setDate(new Date());
//            trendToSave.setImportance(task.getImportance());
            trendToSave.setElements(1);
            save(trendToSave);
        }
    }
}
