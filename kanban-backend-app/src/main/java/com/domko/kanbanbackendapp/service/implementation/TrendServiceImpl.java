package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.*;
import com.domko.kanbanbackendapp.repository.BoardRepository;
import com.domko.kanbanbackendapp.repository.TrendRepository;
import com.domko.kanbanbackendapp.service.TrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class TrendServiceImpl implements TrendService {

    private final TrendRepository trendRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public TrendServiceImpl(TrendRepository trendRepository, BoardRepository boardRepository) {
        this.trendRepository = trendRepository;
        this.boardRepository = boardRepository;
    }

    public void addTrend(Task task) {
        Optional<Trend> trend = trendRepository.findByColumnIdAndDate(task.getColumn().getId(), new Date());
        if (trend.isPresent()) {
            trend.get().setElements(trend.get().getElements() + 1);
            trendRepository.save(trend.get());
        } else {
            Trend trendToSave = new Trend();
            trendToSave.setColumn(task.getColumn());
            trendToSave.setDate(new Date());
            trendToSave.setElements(1);
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

        board.getColumns().forEach((column) -> {
            List<Trend> trends = trendRepository.findAllByColumnIdOrderByDate(column.getId());
            int j = 0;
            ColumnSeries series = new ColumnSeries(seriesSet.getDates().size(), "area");
            series.setName(column.getName());
            for (int i = 0; i < seriesSet.getDates().size(); i++) {
                if (j == trends.size()) {
                    break;
                }
                if (seriesSet.getDates().get(i).substring(0, 10).equals(simpleDateFormat.format(trends.get(j).getDate()))) {
                    series.set(i, trends.get(j).getElements());
                    j++;
                }
            }
            seriesSet.add(series);
        });

    }

    private void prepareTrendLines(Board board, SeriesSet seriesSet) {
        TrendSeries trendSeries = new TrendSeries("Linia trendu", "line");
        TrendSeries arrivalOfTasksSeries = new TrendSeries("Tempo przybywania", "line");
        Random random = new Random();
        List<Integer> trends = new ArrayList<>();
        List<Integer> arrivals = new ArrayList<>();
//        List<Float> dots = new ArrayList<>();
        for (int i = 0; i < seriesSet.getDates().size(); i++) {
//            Integer rand = random.nextInt(80);
            trends.add(random.nextInt(100));
            arrivals.add(random.nextInt(100));
//            dots.add(rand.floatValue());
//            trends.add(seriesSet.getDates().size()-i);
//            dots.add((float) seriesSet.getDates().size()-i);
        }
        List<BigDecimal> trendsBestFitLine = getBestFitLine(trends);
        trendSeries.addAll(trendsBestFitLine);
        seriesSet.add(trendSeries);

        List<BigDecimal> arrivalsBestFitLine = getBestFitLine(arrivals);
        arrivalOfTasksSeries.addAll(arrivalsBestFitLine);
        seriesSet.add(arrivalOfTasksSeries);
    }

    private List<BigDecimal> getBestFitLine(List<Integer> list) {
        BigDecimal xSum = new BigDecimal(0);
        BigDecimal ySum = new BigDecimal(0);

        for (int i = 0; i < list.size(); i++) {
            xSum = xSum.add( new BigDecimal(i + 1));
            ySum = ySum.add(new BigDecimal(list.get(i)));
        }
        xSum = xSum.divide(new BigDecimal(list.size()),RoundingMode.HALF_UP);
        ySum = ySum.divide(new BigDecimal(list.size()),RoundingMode.HALF_UP);
        BigDecimal sumOfSquaredDeviation = new BigDecimal(0);
        System.out.println(sumOfSquaredDeviation.scale());
        BigDecimal sumOfMultipliedDeviations = new BigDecimal(0);
        for (int i = 0; i < list.size(); i++) {
            sumOfMultipliedDeviations =sumOfMultipliedDeviations.add(new BigDecimal ((i + 1 - xSum.doubleValue()) * (list.get(i) - ySum.doubleValue())));
            sumOfSquaredDeviation = sumOfSquaredDeviation.add(new BigDecimal((Math.pow((i + 1 - xSum.doubleValue()), 2))));
        }
        BigDecimal m = sumOfMultipliedDeviations.divide(sumOfSquaredDeviation,RoundingMode.HALF_UP);
        System.out.println(m);
        BigDecimal b = (ySum.subtract(m.multiply(xSum)));
        List<BigDecimal> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("##.##");
        for (int i = 0; i < list.size(); i++) {
//            double y = m * (i + 1) + b;
            BigDecimal y= m.multiply(new BigDecimal(i+1)).add(b);
//            BigDecimal bd = new BigDecimal(y).setScale(2, RoundingMode.HALF_UP);
            result.add(y);
        }
        return result;
    }

}
