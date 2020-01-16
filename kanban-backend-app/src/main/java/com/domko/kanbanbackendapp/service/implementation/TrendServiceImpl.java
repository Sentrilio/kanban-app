package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.model.Trend;
import com.domko.kanbanbackendapp.repository.TrendRepository;
import com.domko.kanbanbackendapp.service.TrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@org.springframework.transaction.annotation.Transactional
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
}
