package com.domko.kanbanbackendapp.model;

import lombok.Data;
import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SeriesSet {
    private List<Series> seriesList = new ArrayList<>();
    private List<String> dates;

    public void add(Series series) {
        seriesList.add(series);
    }

    public void prepareDates(Board board) {
        List<String> dates = new ArrayList<>();
        Date startDate = board.getCreateDate();
        Date today = new Date();
        LocalDateTime dateTimeStart = new LocalDateTime(startDate);
        LocalDateTime dateTimeEnd = new LocalDateTime(today);
        dateTimeStart = dateTimeStart.withTime(0, 0, 0, 0);
        dateTimeEnd = dateTimeEnd.withTime(0, 0, 0, 0);
        dateTimeEnd = dateTimeEnd.plusDays(1);
        int counter = 0;
        while (!dateTimeStart.plusDays(counter).equals(dateTimeEnd)) {
//            System.out.println(dateTimeStart.plusDays(counter).toString());
            dates.add(dateTimeStart.plusDays(counter).toString() + "Z");
            counter++;
        }
        setDates(dates);
    }

    @Override
    public String toString() {
        return "SeriesSet{" +
                "seriesList=\n" + seriesList + "\n" +
                ", dates=" + dates +
                '}';
    }
}
